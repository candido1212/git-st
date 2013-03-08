package com.borland.starteam.impl._private_;

import System.Exception;
import com.borland.starteam.impl.CheckoutManager;
import com.borland.starteam.impl.File;
import com.borland.starteam.impl.FileSyncInfo;
import com.borland.starteam.impl.MakeVisible;
import com.borland.starteam.impl.SDKRuntimeException;
import com.borland.starteam.impl.Server;
import com.borland.starteam.impl.ServerSession;
import com.borland.starteam.impl._private_.vts.comm.Command;
import com.borland.starteam.impl._private_.vts.comm.CommandMacro;
import com.borland.starteam.impl._private_.vts.comm.CommandRoute;
import com.borland.starteam.impl._private_.vts.comm.Connection;
import com.borland.starteam.impl._private_.vts.pickle.Delta;
import com.borland.starteam.impl._private_.vts.pickle.DiffArg;
import com.borland.starteam.impl._private_.vts.pickle.FileRevisionID;
import com.borland.starteam.impl._private_.vts.pickle.ItemRevision;
import com.borland.starteam.impl._private_.vts.pickle.KeywordValues;
import com.borland.starteam.impl._private_.vts.pickle.ViewMemberConfig;
import com.borland.starteam.impl.util.Assert;
import com.borland.starteam.impl.util.GUID;
import com.borland.starteam.impl.util.MD5;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.zip.GZIPInputStream;

public class WfCmdCheckOutInputStream extends CommandMacro {
	private byte[] tmp = new byte[1];
	private GUID m_sessionID;
	private int m_viewState;
	private int m_componentID;
	private Connection m_connection = null;
	private Command m_Cmd = null;
	private double m_FileTime = 0.0D;
	private boolean m_fileExecutable = false;
	private FileRevisionID m_revisionID = null;
	private MD5 m_fileMD5 = null;
	private MD5 m_syncMD5 = null;
	InputStream m_fileStream = null;
	private boolean m_Started = false;
	private boolean m_Done = false;
	private KeywordValues m_keywordValues = null;
	private ItemRevision[] m_history = null;
	private Hashtable m_userMap;
	private Hashtable m_viewMap;
	private int m_exclusiveLockerID = -1;
	private int m_myLockState = -1;
	private int m_storageType = -1;
	private boolean finished = false;
	private static final CommandRoute m_route = new CommandRoute(-2147483648, 0, 10104, "FILE_CMD_CHECKOUT");
	
	final private boolean m_getKeywords;
	final private boolean m_deltasOK;
	final private int m_itemId;
	final private ViewMemberConfig m_vmconfig;
	final private FileSyncInfo m_syncInfo;
	final private MD5 m_localMD5;
	final private boolean m_bVerifyStatus;
	final private boolean m_bLocalMods;

	public boolean isRetrySupported() {
		return false;
	}
	
	public WfCmdCheckOutInputStream(Server server, int itemId, int componentId, int viewSessionId, boolean deltasOK, ViewMemberConfig vmconfig, FileSyncInfo syncInfo, MD5 localMD5, boolean bVerifyStatus, boolean bLocalMods) throws IOException {
		this.m_itemId = itemId;
		this.m_vmconfig = vmconfig;
		this.m_syncInfo = syncInfo;
		this.m_localMD5 = localMD5;
		this.m_bVerifyStatus = bVerifyStatus;
		this.m_bLocalMods = bLocalMods;
		this.m_getKeywords = false; // TODO: figure out if this will ever be useful
		this.m_deltasOK = deltasOK;
		
		this.m_connection = MakeVisible.classServer_useConnection(server);
		this.m_sessionID = server.getSession().getID();
		this.m_viewState = viewSessionId;
		this.m_componentID = componentId;
		initialize();
	}

	public double getFileTime() {
		return this.m_FileTime;
	}

	public boolean getFileExecutable() {
		return this.m_fileExecutable;
	}

	public FileRevisionID getFileRevisionID() {
		return this.m_revisionID;
	}

	public MD5 getFileMD5() {
		return this.m_fileMD5;
	}

	public MD5 getSyncMD5() {
		return this.m_syncMD5;
	}

	public KeywordValues getKeywordValues() {
		return this.m_keywordValues;
	}

	ItemRevision[] getHistory() {
		return this.m_history;
	}

	Hashtable getUserMap() {
		return this.m_userMap;
	}

	Hashtable getViewMap() {
		return this.m_viewMap;
	}

	int getExclusiveLockerID() {
		return this.m_exclusiveLockerID;
	}

	int getMyLockState() {
		return this.m_myLockState;
	}

	private void initialize() throws IOException {
		String cmd = getClass().getName().replaceAll("\\.Wf", ".");
		this.m_connection.usingCommand(cmd);
		this.m_Cmd = prepare(this.m_connection, this.m_sessionID, this.m_viewState, this.m_componentID);
		push(this.m_Cmd);
		this.m_connection.execCommand(this.m_Cmd);
		int k;
		int n;
		if (m_getKeywords) {
			this.m_keywordValues = KeywordValues.read(this.m_Cmd);
			int i = this.m_Cmd.readInt();
			ItemRevision[] arrayOfItemRevision = new ItemRevision[i];
			for (k = 0; k < i; k++)
				arrayOfItemRevision[k] = ItemRevision.read(this.m_Cmd);
			this.m_history = arrayOfItemRevision;
			this.m_userMap = new Hashtable();
			this.m_viewMap = new Hashtable();
			if (!m_deltasOK) {
				i = this.m_Cmd.readInt();
				for (int m = 0; m < i; m++) {
					n = this.m_Cmd.readInt();
					String str1 = this.m_Cmd.readString();
					this.m_userMap.put(new Integer(n), str1);
				}
				i = this.m_Cmd.readInt();
				for (n = 0; n < i; n++) {
					int i1 = this.m_Cmd.readInt();
					String str2 = this.m_Cmd.readString();
					this.m_viewMap.put(new Integer(i1), str2);
				}
			}
		}
		this.m_storageType = this.m_Cmd.readInt();
		if (this.m_storageType == 3) {
			this.m_Done = false;
			this.m_Started = true;
			try {
				this.m_Cmd.setHasGZIPContents(true);
				try {
					this.m_fileStream = new GZIPInputStream(this.m_Cmd.getChunkStream());
				}
				catch (RuntimeException localRuntimeException) {
					restoreCompression();
					throw localRuntimeException;
				}
				catch (Throwable localThrowable) {
					restoreCompression();
					throw new SDKRuntimeException(localThrowable);
				}
			}
			catch (Exception localException) {
				restoreCompression();
				if ((localException instanceof RuntimeException))
					throw localException;
				throw new SDKRuntimeException(localException.get_Message());
			}
		}
		else if (this.m_storageType == 2) {
			int j = this.m_Cmd.readInt();
			Delta[] arrayOfDelta = new Delta[j];
			for (k = 0; k < j; k++)
				arrayOfDelta[k] = Delta.readFromWire(this.m_Cmd);
			applyDeltasToFileStream(arrayOfDelta);
			this.m_fileMD5 = this.m_Cmd.readMD5();
		}
		else if (this.m_storageType != 1) {
			Assert.internalError("CmdCheckOutInputStream.initialize(), returnType=" + this.m_storageType + ".");
		}
		else {
			this.m_Done = false;
			this.m_Started = true;
			this.m_fileStream = this.m_Cmd.getChunkStream();
		}
	}

	private void applyDeltasToFileStream(Delta[] paramArrayOfDelta) throws IOException {
		throw new RuntimeException("Not implemented");
		/*
		Object localObject1 = new DiffArg(paramArrayOfDelta[0].m_fileInfo);
		long l = this.m_file.getLocalSize();
		if (l > 2147483647L)
			throw new SDKRuntimeException(Res.formatKey("FILE_TOO_LARGE_FMT", new Object[] { new Long(l) }));
		byte[] arrayOfByte = new byte[(int) l];
		FileInputStream localFileInputStream = null;
		try {
			localFileInputStream = new FileInputStream(this.m_file.getFullName());
			localFileInputStream.read(arrayOfByte);
		}
		finally {
			localFileInputStream.close();
		}
		((DiffArg) localObject1).create(arrayOfByte);
		for (int i = 0; i < paramArrayOfDelta.length; i++) {
			DiffArg localDiffArg = new DiffArg();
			paramArrayOfDelta[i].construct((DiffArg) localObject1, localDiffArg);
			localObject1 = localDiffArg;
		}
		this.m_Done = false;
		this.m_Started = true;
		this.m_fileStream = new ByteArrayInputStream(((DiffArg) localObject1).getByteArray());
		*/
	}

	void finish() throws IOException {
		if (this.finished)
			return;
		this.finished = true;
		restoreCompression();
		// if (!this.m_request.getCheckoutManager().isCanceled()) {
			this.m_FileTime = this.m_Cmd.readTime();
			this.m_myLockState = this.m_Cmd.readInt();
			this.m_exclusiveLockerID = this.m_Cmd.readInt();
			this.m_revisionID = FileRevisionID.read(this.m_Cmd);
			this.m_fileMD5 = this.m_Cmd.readMD5();
			this.m_syncMD5 = this.m_fileMD5;
			this.m_fileExecutable = this.m_Cmd.readBoolean();
		//}
		try {
			/* this.m_Cmd.m_isCanceled = this.m_request.getCheckoutManager().isCanceled(); */
			this.m_Cmd.m_isCanceled = false;
			this.m_connection.terminateCommand(this.m_Cmd);
		}
		catch (IOException localIOException) {
			this.m_connection.onCommandException(localIOException);
			throw localIOException;
		}
		finally {
			this.m_connection.commandNotInUse(); // TODO: is this the best place for this?
		}
	}

	private void restoreCompression() {
		if (this.m_Cmd.hasGZIPContents()) {
			this.m_Cmd.setHasGZIPContents(false);
			this.m_Cmd.setReadingGZIPContents(false);
		}
	}

	int read() throws IOException {
		return read(this.tmp, 0, 1) == -1 ? -1 : this.tmp[0] & 0xFF;
	}

	int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
		if (!this.m_Started)
			initialize();
		if (this.m_Done)
			return -1;
		int i = 0;
		try {
			int j = this.m_fileStream.read(paramArrayOfByte, paramInt1, paramInt2);
			if (j == -1) {
				this.m_Done = true;
				finish();
				return -1;
			}
			return j;
		}
		catch (IOException localIOException) {
			restoreCompression();
			this.m_Cmd.onError(localIOException);
			i = 1;
			throw localIOException;
		}
		catch (RuntimeException localRuntimeException) {
			restoreCompression();
			this.m_Cmd.onError(localRuntimeException);
			i = 1;
			throw localRuntimeException;
		}
	}

	public void push(Command paramCommand) throws IOException {
		int itemId = m_itemId;
		ViewMemberConfig vmconfig = m_vmconfig;
		FileSyncInfo syncInfo = m_syncInfo;
		boolean bVerifyStatus = m_bVerifyStatus;
		boolean bDeltasOk = m_deltasOK;
		boolean bLocalMods = m_bLocalMods;
		MD5 localMD5 = m_localMD5;
		boolean bGetKeywords = m_getKeywords;
		
		int id = itemId;
		paramCommand.writeInt(id);
		vmconfig.writeGuts(paramCommand);
		paramCommand.writeInt(3);
		if ((syncInfo == null) || (syncInfo.getRevisionIdentifier() == null) || (localMD5 == null)) {
			bVerifyStatus = false;
			bDeltasOk = false;
		}
		paramCommand.writeBoolean(bVerifyStatus);
		paramCommand.writeBoolean(bDeltasOk);
		if ((bVerifyStatus) || (bDeltasOk)) {
			paramCommand.writeInt(syncInfo.getRevisionIdentifier().getRootObjectID());
			paramCommand.writeInt(syncInfo.getRevisionIdentifier().getObjectID());
			paramCommand.writeInt(syncInfo.getRevisionIdentifier().getRevisionNumber());
			paramCommand.writeString(syncInfo.getRevisionIdentifier().getDotNotation());
			paramCommand.writeInt(syncInfo.getRevisionIdentifier().getContentRevision());
			paramCommand.writeBoolean(bLocalMods);
			paramCommand.writeMD5(localMD5);
		}
		paramCommand.writeBoolean(bGetKeywords);
		if (paramCommand.getRevisionLevel() >= 51) {
			if (bDeltasOk) {
				paramCommand.writeBoolean(false);
			}
			else {
				paramCommand.writeBoolean(true);
			}
		}
	}

	public void pop(Command paramCommand) throws IOException {
	}

	public CommandRoute getCommandRoute() {
		return m_route;
	}
}
