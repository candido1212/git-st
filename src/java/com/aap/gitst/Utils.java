package com.aap.gitst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aap.gitst.Logger.ProgressBar;
import com.aap.gitst.fastimport.FileData;
import com.borland.starteam.impl.Internals;
import com.borland.starteam.impl.MakeVisible;
import com.borland.starteam.impl._private_.WfCheckOutInputStream;
import com.borland.starteam.impl._private_.vts.pickle.ViewMemberConfig;
import com.starbase.starteam.CheckoutListener;
import com.starbase.starteam.CheckoutManager;
import com.starbase.starteam.Item;
import com.starbase.starteam.ItemList;
import com.starbase.starteam.Server;
import com.starbase.util.OLEDate;

/**
 * @author Andrey Pavlenko
 */
public class Utils {
    private static final boolean USE_INTERNALS = !"false"
            .equalsIgnoreCase(System.getenv("GITST_USE_INTERNALS"));
    private static Method GET_HISTORY12;
    private static final boolean IS_API12;

    static {
        Method getHistory = null;

        if (USE_INTERNALS) {
            try {
                Class.forName("com.starteam.Item");
                getHistory = Class.forName("com.starteam.Internals12")
                        .getMethod("getHistory", Repo.class, Item.class);
            } catch (final Throwable ex) {
            }
        }

        GET_HISTORY12 = getHistory;
        IS_API12 = getHistory != null;
    }

    public static String bytesToString(final long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < (1024 * 1024)) {
            return (bytes / 1024) + " KB";
        } else {
            return new BigDecimal((double) bytes / (1024 * 1024)).setScale(1,
                    RoundingMode.HALF_UP) + " MB";
        }
    }

    public static void copyFile(final java.io.File from, final java.io.File to)
            throws FileNotFoundException, IOException {
        try (FileInputStream in = new FileInputStream(from);
                FileOutputStream out = new FileOutputStream(to);
                FileChannel inc = in.getChannel();
                FileChannel outc = out.getChannel();) {
            final int maxCount = (64 * 1024 * 1024) - (32 * 1024);
            final long size = inc.size();
            long position = 0;
            while (position < size) {
                position += inc.transferTo(position, maxCount, outc);
            }
        }
    }

    public static String getParentFolderPath(final String path) {
        final int slash = path.lastIndexOf('/');

        if (slash == -1) {
            return "";
        } else {
            return path.substring(0, slash);
        }
    }

    public static boolean isApi12() {
        return IS_API12;
    }

    public static com.starbase.starteam.Item[] getHistory(final Repo repo,
            final com.starbase.starteam.Item i,
            final com.starbase.starteam.ItemList list) {
        if (i.isDeleted()) {
            final double lastModified = i.getModifiedTime().getDoubleValue();
            final double deletion = i.getDeletedTime().getDoubleValue();
            final double viewCreation = i.getView().getCreatedTime().getDoubleValue();
            final double notBefore = (lastModified < viewCreation) ? viewCreation : lastModified;
            
            final com.starbase.starteam.View historyView;
            
            historyView = repo.getView(i.getView(), notBefore, deletion);

            // FIXME: this is a workaround to avoid unexpected failures
            // during checkout of deleted files.
            com.starbase.starteam.Item historyItem = historyView.findItem(
                    i.getType(), i.getItemID());

            if (historyItem == null) {
                historyItem = i;
            }

            final com.starbase.starteam.Item[] history = getHistory(repo,
                    historyItem);
            final com.starbase.starteam.Item[] h = new com.starbase.starteam.Item[history.length + 1];
            h[0] = i; // Marker for deleted items

            for (int n = 0; n < history.length; n++) {
                h[n + 1] = history[n];
                list.addItem(history[n]);
            }

            return h;
        } else {
            final com.starbase.starteam.Item[] history = getHistory(repo, i);

            for (final com.starbase.starteam.Item h : history) {
                list.addItem(h);
            }

            return history;
        }
    }

    public static Item[] getHistory(final Repo repo, final Item i) {
        if (!USE_INTERNALS) {
            return i.getHistory();
        } else if (GET_HISTORY12 != null) {
            try {
                return (Item[]) GET_HISTORY12.invoke(null, repo, i);
            } catch (final Exception ex) {
                throw new RuntimeException(ex);
            }
        } else {
            return Internals.getHistory(repo, i);
        }
    }
    
    public static void checkout(final Repo repo, final ItemList items, 
            final Map<RemoteFile, List<FileData>> filesMap, ProgressBar pbar) throws InterruptedException {
        if (isApi12()) {
            //checkout12(repo, items, filesMap);
        } else {
            //final CheckoutManager mgr = repo.createCheckoutManager();
            //mgr.addCheckoutListener(listener);
            int count = items.size();
            Server server = repo.getIdleConnection();
            byte[] buffer = new byte[16384];
            for (int i = 0; i < count; i++) {
                Item item = items.getAt(i);
                final RemoteFile id = new RemoteFile((com.starbase.starteam.File)item);
                final List<FileData> fileData = filesMap.get(id);
                File cacheFile;
                try {
                    byte[] md5b = ((com.starbase.starteam.File)item).getMD5();
                    String md5 = toMd5String(md5b);
                    String fldr = md5.substring(md5.length() - 2, md5.length());
                    cacheFile = new File(new File(repo.getCacheDir(), fldr), md5);
                    ProgressBar fpbar = null;
                    if (!cacheFile.isFile()) {
                        int size = ((com.starbase.starteam.File)item).getSize();
                        if (size > 1 << 20)
                            fpbar = repo.getLogger().createProgressBar(repo.getPath(item), size);
                        ViewMemberConfig vmconfig = new ViewMemberConfig();
                        vmconfig.setRevision(item.getObjectID(), item.getRevisionNumber());
                        File tempFile = new File(cacheFile.getPath() + "~");
                        WfCheckOutInputStream s = new WfCheckOutInputStream(
                                server, 
                                item.getID(), 
                                MakeVisible.classFile_getRoutingComponentID((com.starbase.starteam.File)item),
                                MakeVisible.classServer_getViewSession(server, item.getView()).getID(), 
                                false, 
                                vmconfig,
                                null, 
                                null, 
                                false, 
                                false);
                        try {
                            int len;
                            int total = 0;
                            File dir = cacheFile.getParentFile();
                            if (!dir.isDirectory())
                                dir.mkdirs();
                            FileOutputStream out = new FileOutputStream(tempFile);
                            try {
                                while (-1 != (len = s.read(buffer, 0, buffer.length))) {
                                    out.write(buffer, 0, len);
                                    total += len;
                                    if (fpbar != null)
                                        fpbar.done(total);
                                }
                            }
                            finally {
                                out.close();
                            }
                        }
                        finally {
                            s.close();
                        }
                        tempFile.renameTo(cacheFile);
                        if (fpbar != null)
                            fpbar.complete();
                    }
                    if (fileData.size() == 1) {
                        fileData.get(0).setCheckout(cacheFile);
                    }
                    else {
                        System.err.println(fileData.size());
                    }
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                pbar.done(i + 1);
            }
            pbar.complete();
        }
    }
    
    private static final char[] hexchar = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' }; 
    
    private static String toMd5String(byte[] md5) {
        char[] chars = new char[md5.length * 2];
        for (int i = 0; i < md5.length; i++) {
            chars[i * 2] = hexchar[(md5[i] & 0xF0) >> 4];
            chars[i * 2 + 1] = hexchar[md5[i] & 0xF];
        }
        return new String(chars);
    }
}
