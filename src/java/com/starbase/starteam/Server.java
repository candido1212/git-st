package com.starbase.starteam;

import com.borland.starteam.impl.DisembodiedException;
import com.borland.starteam.impl._private_._PrivateMethods_;
import com.borland.starteam.impl._private_.util.WeakReference;
import com.borland.starteam.util.Encoding;
import com.borland.starteam.xml.XMLAttribute;
import com.borland.starteam.xml.XMLDocument;
import com.borland.starteam.xml.XMLElement;
import com.borland.starteam.xml.XMLException;
import com.starbase.diff.BasicColumnMask;
import com.starbase.diff.BasicCompare;
import com.starbase.diff.BinaryCompare;
import com.starbase.diff.ConsoleWriter;
import com.starbase.diff.DiffException;
import com.starbase.diff.Edit;
import com.starbase.diff.EditList;
import com.starbase.diff.EditListWriter;
import com.starbase.diff.HTMLDiffWriter;
import com.starbase.diff.HTMLInterleavedDiffWriter;
import com.starbase.diff.HTMLStereoDiffWriter;
import com.starbase.diff.IntralineSequence;
import com.starbase.diff.LineIterator;
import com.starbase.diff.ParsedCharSequence;
import com.starbase.diff.SharedCharSequence;
import com.starbase.diff.StarTeamDiff;
import com.starbase.diff.StringCharSequence;
import com.starbase.starteam._private_.notify.MPXStatusListenerImpl;
import com.starbase.starteam._private_.notify.ProjectListenerImpl;
import com.starbase.starteam._private_.notify.ServerControlListenerImpl;
import com.starbase.starteam._private_.notify.TypeListenerImpl;
import com.starbase.starteam._private_.notify.UserListenerImpl;
import com.starbase.starteam.viewcomparemerge.ActionOverride;
import com.starbase.starteam.viewcomparemerge.AnyScope;
import com.starbase.starteam.viewcomparemerge.Custom3WayMergeAdapter;
import com.starbase.starteam.viewcomparemerge.DifferenceType;
import com.starbase.starteam.viewcomparemerge.EveryScope;
import com.starbase.starteam.viewcomparemerge.FileMergeAdapter;
import com.starbase.starteam.viewcomparemerge.FolderScope;
import com.starbase.starteam.viewcomparemerge.ItemDifference;
import com.starbase.starteam.viewcomparemerge.ItemListScope;
import com.starbase.starteam.viewcomparemerge.ItemTypeScope;
import com.starbase.starteam.viewcomparemerge.MatchState;
import com.starbase.starteam.viewcomparemerge.MergeAction;
import com.starbase.starteam.viewcomparemerge.MergeType;
import com.starbase.starteam.viewcomparemerge.Options;
import com.starbase.starteam.viewcomparemerge.PreviewState;
import com.starbase.starteam.viewcomparemerge.ProcessItemScope;
import com.starbase.starteam.viewcomparemerge.Progress;
import com.starbase.starteam.viewcomparemerge.RevisionLabelScope;
import com.starbase.starteam.viewcomparemerge.Session;
import com.starbase.starteam.viewcomparemerge.SessionState;
import com.starbase.starteam.viewcomparemerge.StarTeamFileMergeAdapter;
import com.starbase.starteam.viewcomparemerge.VisMergeAdapter;
import com.starbase.starteam.viewcomparemerge.WorkstationDefaultMergeAdapter;
import com.starbase.starteam.viewcomparemerge._PrivateMthds_;
import com.starbase.starteam.vts.comm.Base64InputStream;
import com.starbase.starteam.vts.comm.BinaryChunkInputStream;
import com.starbase.starteam.vts.comm.Connection;
import com.starbase.starteam.vts.comm.EncryptionAlgorithm;
import com.starbase.starteam.vts.comm.ServerCommandEvent;
import com.starbase.util.Assert;
import com.starbase.util.EOLReader;
import com.starbase.util.FileAccess;
import com.starbase.util.FileBasedOptions;
import com.starbase.util.GUID;
import com.starbase.util.LineProcessor;
import com.starbase.util.LineReader;
import com.starbase.util.MD5;
import com.starbase.util.MD5Stream;
import com.starbase.util.OLEDate;
import com.starbase.util.OLEDateFormat;
import com.starbase.util.Platform;
import com.starbase.util.StringCompare;
import com.starbase.util.TextInputStream;
import com.starbase.util.UTF8InputStreamReader;
import com.starbase.util.UnixFileAccess;
import com.starbase.util.WintelOptions;
import com.starbase.util.XMLUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.WeakHashMap;

public class Server {
    private static Hashtable m_servers = new Hashtable();
    private static Hashtable m_constructors = new Hashtable();
    private static Hashtable m_methods = new Hashtable();
    protected com.borland.starteam.impl.Server m_wrap = null;
    private Hashtable m_listeners = new Hashtable();

    static RuntimeException wrap(RuntimeException paramRuntimeException) {
        RuntimeException localObject = null;
        if ((paramRuntimeException instanceof com.borland.starteam.impl.CommandAbortedException))
            localObject = CommandAbortedException
                    .wrap((com.borland.starteam.impl.CommandAbortedException) paramRuntimeException);
        else if ((paramRuntimeException instanceof com.borland.starteam.impl.ConnectionException))
            localObject = com.starbase.starteam.vts.comm.ConnectionException
                    .wrap((com.borland.starteam.impl.ConnectionException) paramRuntimeException);
        else if ((paramRuntimeException instanceof com.borland.starteam.impl.CommandException))
            localObject = com.starbase.starteam.vts.comm.CommandException
                    .wrap((com.borland.starteam.impl.CommandException) paramRuntimeException);
        else if ((paramRuntimeException instanceof DisembodiedException))
            localObject = DisembodiedItemException
                    .wrap((DisembodiedException) paramRuntimeException);
        else if ((paramRuntimeException instanceof com.borland.starteam.impl.ForeignPasswordException))
            localObject = ForeignPasswordException
                    .wrap((com.borland.starteam.impl.ForeignPasswordException) paramRuntimeException);
        else if ((paramRuntimeException instanceof com.borland.starteam.impl.ItemNotFoundException))
            localObject = ItemNotFoundException
                    .wrap((com.borland.starteam.impl.ItemNotFoundException) paramRuntimeException);
        else if ((paramRuntimeException instanceof com.borland.starteam.impl.LogonException))
            localObject = LogonException
                    .wrap((com.borland.starteam.impl.LogonException) paramRuntimeException);
        else if ((paramRuntimeException instanceof com.borland.starteam.impl.PasswordExpiredException))
            localObject = PasswordExpiredException
                    .wrap((com.borland.starteam.impl.PasswordExpiredException) paramRuntimeException);
        else if ((paramRuntimeException instanceof com.borland.starteam.impl.NoSuchPropertyException))
            localObject = NoSuchPropertyException
                    .wrap((com.borland.starteam.impl.NoSuchPropertyException) paramRuntimeException);
        else if ((paramRuntimeException instanceof com.borland.starteam.impl.ServerUnavailableException))
            localObject = ServerUnavailableException
                    .wrap((com.borland.starteam.impl.ServerUnavailableException) paramRuntimeException);
        else if ((paramRuntimeException instanceof com.borland.starteam.impl.TypeNotFoundException))
            localObject = TypeNotFoundException
                    .wrap((com.borland.starteam.impl.TypeNotFoundException) paramRuntimeException);
        else if ((paramRuntimeException instanceof com.borland.starteam.impl.SDKRuntimeException))
            localObject = SDKRuntimeException
                    .wrap((com.borland.starteam.impl.SDKRuntimeException) paramRuntimeException);
        else if ((paramRuntimeException instanceof com.borland.starteam.impl.util.WorkStationException))
            localObject = com.starbase.util.WorkStationException
                    .wrap((com.borland.starteam.impl.util.WorkStationException) paramRuntimeException);
        else if ((paramRuntimeException instanceof com.borland.starteam.impl.ServerException))
            localObject = ServerException
                    .wrap((com.borland.starteam.impl.ServerException) paramRuntimeException);
        else if ((paramRuntimeException instanceof com.borland.starteam.impl.PropertyNotWritableException))
            localObject = PropertyNotWritableException
                    .wrap((com.borland.starteam.impl.PropertyNotWritableException) paramRuntimeException);
        else if ((paramRuntimeException instanceof com.borland.starteam.impl.viewcomparemerge.AmbiguousMatchException))
            localObject = com.starbase.starteam.viewcomparemerge.AmbiguousMatchException
                    .wrap((com.borland.starteam.impl.viewcomparemerge.AmbiguousMatchException) paramRuntimeException);
        else if ((paramRuntimeException instanceof com.borland.starteam.impl.viewcomparemerge.FileMergeException))
            localObject = com.starbase.starteam.viewcomparemerge.FileMergeException
                    .wrap((com.borland.starteam.impl.viewcomparemerge.FileMergeException) paramRuntimeException);
        else if ((paramRuntimeException instanceof com.borland.starteam.impl.viewcomparemerge.PropertyMergeException))
            localObject = com.starbase.starteam.viewcomparemerge.PropertyMergeException
                    .wrap((com.borland.starteam.impl.viewcomparemerge.PropertyMergeException) paramRuntimeException);
        else
            localObject = paramRuntimeException;
        return localObject;
    }

    static Object cloak(Server paramServer, Class paramClass1,
            Class paramClass2, Object paramObject) {
        if (!m_servers.containsKey(paramServer))
            m_servers.put(paramServer, new Hashtable());
        Hashtable localHashtable = (Hashtable) m_servers.get(paramServer);
        if (!localHashtable.containsKey(paramClass1))
            localHashtable.put(paramClass1, new WeakHashMap());
        WeakHashMap localWeakHashMap = (WeakHashMap) localHashtable
                .get(paramClass1);
        Object localObject1 = null;
        Object localObject2;
        if (localWeakHashMap.containsKey(paramObject)) {
            localObject2 = localWeakHashMap.get(paramObject);
            if (localObject2 != null)
                localObject1 = ((WeakReference) localObject2).get();
        }
        if (localObject1 != null) {
            localObject2 = null;
            if ((localObject1 instanceof TypedResource)) {
                if ((localObject1 instanceof com.starbase.starteam.viewcomparemerge.MergePreview))
                    localObject2 = _PrivateMthds_
                            .unwrapMergePreview((com.starbase.starteam.viewcomparemerge.MergePreview) localObject1);
                else if ((localObject1 instanceof com.starbase.starteam.viewcomparemerge.VCMFolder))
                    localObject2 = _PrivateMthds_
                            .unwrapVCMFolder((com.starbase.starteam.viewcomparemerge.VCMFolder) localObject1);
                else
                    localObject2 = ((TypedResource) localObject1).unwrap();
            } else if ((localObject1 instanceof Property)) {
                localObject2 = ((Property) localObject1).unwrap();
            } else if ((localObject1 instanceof Type)) {
                localObject2 = ((Type) localObject1).unwrap();
            } else if (Platform.isDotNET()) {
                if ((localObject1 instanceof AccessCheckSecLogID))
                    localObject2 = ((AccessCheckSecLogID) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof AccessRightsManager))
                    localObject2 = ((AccessRightsManager) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof AccessTestResults))
                    localObject2 = ((AccessTestResults) localObject1).unwrap();
                else if ((localObject1 instanceof AclEntry))
                    localObject2 = ((AclEntry) localObject1).unwrap();
                else if ((localObject1 instanceof AddResult))
                    localObject2 = ((AddResult) localObject1).unwrap();
                else if ((localObject1 instanceof BLSLicenseInfo))
                    localObject2 = ((BLSLicenseInfo) localObject1).unwrap();
                else if ((localObject1 instanceof BooleanPropertyWrapper))
                    localObject2 = ((BooleanPropertyWrapper) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof CacheAgent))
                    localObject2 = ((CacheAgent) localObject1).unwrap();
                else if ((localObject1 instanceof CacheAgentEvent))
                    localObject2 = ((CacheAgentEvent) localObject1).unwrap();
                else if ((localObject1 instanceof CheckinEvent))
                    localObject2 = ((CheckinEvent) localObject1).unwrap();
                else if ((localObject1 instanceof CheckinManager))
                    localObject2 = ((CheckinManager) localObject1).unwrap();
                else if ((localObject1 instanceof CheckinOptions))
                    localObject2 = ((CheckinOptions) localObject1).unwrap();
                else if ((localObject1 instanceof CheckinPhase))
                    localObject2 = ((CheckinPhase) localObject1).unwrap();
                else if ((localObject1 instanceof CheckinProgress))
                    localObject2 = ((CheckinProgress) localObject1).unwrap();
                else if ((localObject1 instanceof CheckInResult))
                    localObject2 = ((CheckInResult) localObject1).unwrap();
                else if ((localObject1 instanceof CheckoutEvent))
                    localObject2 = ((CheckoutEvent) localObject1).unwrap();
                else if ((localObject1 instanceof CheckoutManager))
                    localObject2 = ((CheckoutManager) localObject1).unwrap();
                else if ((localObject1 instanceof CheckoutOptions))
                    localObject2 = ((CheckoutOptions) localObject1).unwrap();
                else if ((localObject1 instanceof CheckoutProgress))
                    localObject2 = ((CheckoutProgress) localObject1).unwrap();
                else if ((localObject1 instanceof CheckOutResult))
                    localObject2 = ((CheckOutResult) localObject1).unwrap();
                else if ((localObject1 instanceof ClientContext))
                    localObject2 = ((ClientContext) localObject1).unwrap();
                else if ((localObject1 instanceof ColumnInfo))
                    localObject2 = ((ColumnInfo) localObject1).unwrap();
                else if ((localObject1 instanceof CommandAbortedException))
                    localObject2 = ((CommandAbortedException) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof ContainerACLSecLogID))
                    localObject2 = ((ContainerACLSecLogID) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof DirectoryOverrides))
                    localObject2 = ((DirectoryOverrides) localObject1).unwrap();
                else if ((localObject1 instanceof DisembodiedItemException))
                    localObject2 = ((DisembodiedItemException) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof DuplicateServerListEntryException))
                    localObject2 = ((DuplicateServerListEntryException) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof EffectiveACE))
                    localObject2 = ((EffectiveACE) localObject1).unwrap();
                else if ((localObject1 instanceof EmailAttachment))
                    localObject2 = ((EmailAttachment) localObject1).unwrap();
                else if ((localObject1 instanceof EnumeratedValue))
                    localObject2 = ((EnumeratedValue) localObject1).unwrap();
                else if ((localObject1 instanceof EOLFormat))
                    localObject2 = ((EOLFormat) localObject1).unwrap();
                else if ((localObject1 instanceof EventHandlerInfo))
                    localObject2 = ((EventHandlerInfo) localObject1).unwrap();
                else if ((localObject1 instanceof EventHandlerProfile))
                    localObject2 = ((EventHandlerProfile) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof FileBasedStatusManager))
                    localObject2 = ((FileBasedStatusManager) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof FileResult))
                    localObject2 = ((FileResult) localObject1).unwrap();
                else if ((localObject1 instanceof Filter))
                    localObject2 = ((Filter) localObject1).unwrap();
                else if ((localObject1 instanceof FilterEvent))
                    localObject2 = ((FilterEvent) localObject1).unwrap();
                else if ((localObject1 instanceof FolderEvent))
                    localObject2 = ((FolderEvent) localObject1).unwrap();
                else if ((localObject1 instanceof FolderListManager))
                    localObject2 = ((FolderListManager) localObject1).unwrap();
                else if ((localObject1 instanceof FolderTreeEvent))
                    localObject2 = ((FolderTreeEvent) localObject1).unwrap();
                else if ((localObject1 instanceof FolderUpdateEvent))
                    localObject2 = ((FolderUpdateEvent) localObject1).unwrap();
                else if ((localObject1 instanceof ForeignPasswordException))
                    localObject2 = ((ForeignPasswordException) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof ForeignRefreshResult))
                    localObject2 = ((ForeignRefreshResult) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof Group))
                    localObject2 = ((Group) localObject1).unwrap();
                else if ((localObject1 instanceof GroupAccount))
                    localObject2 = ((GroupAccount) localObject1).unwrap();
                else if ((localObject1 instanceof GroupNode))
                    localObject2 = ((GroupNode) localObject1).unwrap();
                else if ((localObject1 instanceof GroupSortInfo))
                    localObject2 = ((GroupSortInfo) localObject1).unwrap();
                else if ((localObject1 instanceof HistoryKeyword))
                    localObject2 = ((HistoryKeyword) localObject1).unwrap();
                else if ((localObject1 instanceof HistoryLine))
                    localObject2 = ((HistoryLine) localObject1).unwrap();
                else if ((localObject1 instanceof HiveInfo))
                    localObject2 = ((HiveInfo) localObject1).unwrap();
                else if ((localObject1 instanceof HiveManager))
                    localObject2 = ((HiveManager) localObject1).unwrap();
                else if ((localObject1 instanceof ImportUtility))
                    localObject2 = ((ImportUtility) localObject1).unwrap();
                else if ((localObject1 instanceof IPRange))
                    localObject2 = ((IPRange) localObject1).unwrap();
                else if ((localObject1 instanceof ItemBehavior))
                    localObject2 = ((ItemBehavior) localObject1).unwrap();
                else if ((localObject1 instanceof ItemEvent))
                    localObject2 = ((ItemEvent) localObject1).unwrap();
                else if ((localObject1 instanceof ItemIDEvent))
                    localObject2 = ((ItemIDEvent) localObject1).unwrap();
                else if ((localObject1 instanceof ItemList))
                    localObject2 = ((ItemList) localObject1).unwrap();
                else if ((localObject1 instanceof ItemListEvent))
                    localObject2 = ((ItemListEvent) localObject1).unwrap();
                else if ((localObject1 instanceof ItemListManager))
                    localObject2 = ((ItemListManager) localObject1).unwrap();
                else if ((localObject1 instanceof ItemNotFoundException))
                    localObject2 = ((ItemNotFoundException) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof ItemReference))
                    localObject2 = ((ItemReference) localObject1).unwrap();
                else if ((localObject1 instanceof ItemRevision))
                    localObject2 = ((ItemRevision) localObject1).unwrap();
                else if ((localObject1 instanceof ItemUpdateEvent))
                    localObject2 = ((ItemUpdateEvent) localObject1).unwrap();
                else if ((localObject1 instanceof KeywordReader))
                    localObject2 = ((KeywordReader) localObject1).unwrap();
                else if ((localObject1 instanceof Label))
                    localObject2 = ((Label) localObject1).unwrap();
                else if ((localObject1 instanceof LabelEvent))
                    localObject2 = ((LabelEvent) localObject1).unwrap();
                else if ((localObject1 instanceof LicenseUsage))
                    localObject2 = ((LicenseUsage) localObject1).unwrap();
                else if ((localObject1 instanceof Link))
                    localObject2 = ((Link) localObject1).unwrap();
                else if ((localObject1 instanceof LinkCache))
                    localObject2 = ((LinkCache) localObject1).unwrap();
                else if ((localObject1 instanceof LinkEndpoint))
                    localObject2 = ((LinkEndpoint) localObject1).unwrap();
                else if ((localObject1 instanceof LinkFactory))
                    localObject2 = ((LinkFactory) localObject1).unwrap();
                else if ((localObject1 instanceof LinkUpdateEvent))
                    localObject2 = ((LinkUpdateEvent) localObject1).unwrap();
                else if ((localObject1 instanceof LogKeyword))
                    localObject2 = ((LogKeyword) localObject1).unwrap();
                else if ((localObject1 instanceof LogLine))
                    localObject2 = ((LogLine) localObject1).unwrap();
                else if ((localObject1 instanceof LogonException))
                    localObject2 = ((LogonException) localObject1).unwrap();
                else if ((localObject1 instanceof LongIntegerPropertyWrapper))
                    localObject2 = ((LongIntegerPropertyWrapper) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof MatchedACE))
                    localObject2 = ((MatchedACE) localObject1).unwrap();
                else if ((localObject1 instanceof MergePoint))
                    localObject2 = ((MergePoint) localObject1).unwrap();
                else if ((localObject1 instanceof MPXException))
                    localObject2 = ((MPXException) localObject1).unwrap();
                else if ((localObject1 instanceof MPXStatusAdapter))
                    localObject2 = ((MPXStatusAdapter) localObject1).unwrap();
                else if ((localObject1 instanceof MPXStatusEvent))
                    localObject2 = ((MPXStatusEvent) localObject1).unwrap();
                else if ((localObject1 instanceof MyUserAccount))
                    localObject2 = ((MyUserAccount) localObject1).unwrap();
                else if ((localObject1 instanceof NoKeywords))
                    localObject2 = ((NoKeywords) localObject1).unwrap();
                else if ((localObject1 instanceof NoSuchPropertyException))
                    localObject2 = ((NoSuchPropertyException) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof NotificationEvent))
                    localObject2 = ((NotificationEvent) localObject1).unwrap();
                else if ((localObject1 instanceof NotificationItem))
                    localObject2 = ((NotificationItem) localObject1).unwrap();
                else if ((localObject1 instanceof ObjectACLSecLogID))
                    localObject2 = ((ObjectACLSecLogID) localObject1).unwrap();
                else if ((localObject1 instanceof PasswordExpiredException))
                    localObject2 = ((PasswordExpiredException) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof ProcessItem))
                    localObject2 = ((ProcessItem) localObject1).unwrap();
                else if ((localObject1 instanceof ProcessItemUsage))
                    localObject2 = ((ProcessItemUsage) localObject1).unwrap();
                else if ((localObject1 instanceof Project))
                    localObject2 = ((Project) localObject1).unwrap();
                else if ((localObject1 instanceof ProjectEvent))
                    localObject2 = ((ProjectEvent) localObject1).unwrap();
                else if ((localObject1 instanceof PromotionModel))
                    localObject2 = ((PromotionModel) localObject1).unwrap();
                else if ((localObject1 instanceof PromotionState))
                    localObject2 = ((PromotionState) localObject1).unwrap();
                else if ((localObject1 instanceof PropertyNotWritableException))
                    localObject2 = ((PropertyNotWritableException) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof PurgeControl))
                    localObject2 = ((PurgeControl) localObject1).unwrap();
                else if ((localObject1 instanceof PurgeStatus))
                    localObject2 = ((PurgeStatus) localObject1).unwrap();
                else if ((localObject1 instanceof QueryEvent))
                    localObject2 = ((QueryEvent) localObject1).unwrap();
                else if ((localObject1 instanceof QueryInfo))
                    localObject2 = ((QueryInfo) localObject1).unwrap();
                else if ((localObject1 instanceof QueryNode))
                    localObject2 = ((QueryNode) localObject1).unwrap();
                else if ((localObject1 instanceof QueryPart))
                    localObject2 = ((QueryPart) localObject1).unwrap();
                else if ((localObject1 instanceof RecycleBin))
                    localObject2 = ((RecycleBin) localObject1).unwrap();
                else if ((localObject1 instanceof RevisionID))
                    localObject2 = ((RevisionID) localObject1).unwrap();
                else if ((localObject1 instanceof SDKRuntimeException))
                    localObject2 = ((SDKRuntimeException) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof SecurityLogEntry))
                    localObject2 = ((SecurityLogEntry) localObject1).unwrap();
                else if ((localObject1 instanceof Server))
                    localObject2 = ((Server) localObject1).unwrap();
                else if ((localObject1 instanceof ServerAdministration))
                    localObject2 = ((ServerAdministration) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof ServerConfiguration))
                    localObject2 = ((ServerConfiguration) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof ServerControlEvent))
                    localObject2 = ((ServerControlEvent) localObject1).unwrap();
                else if ((localObject1 instanceof ServerException))
                    localObject2 = ((ServerException) localObject1).unwrap();
                else if ((localObject1 instanceof ServerInfo))
                    localObject2 = ((ServerInfo) localObject1).unwrap();
                else if ((localObject1 instanceof ServerLicenseInfo))
                    localObject2 = ((ServerLicenseInfo) localObject1).unwrap();
                else if ((localObject1 instanceof ServerList))
                    localObject2 = ((ServerList) localObject1).unwrap();
                else if ((localObject1 instanceof ServerUnavailableException))
                    localObject2 = ((ServerUnavailableException) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof SimpleKeyword))
                    localObject2 = ((SimpleKeyword) localObject1).unwrap();
                else if ((localObject1 instanceof StarTeamClientOptions))
                    localObject2 = ((StarTeamClientOptions) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof StarTeamURL))
                    localObject2 = ((StarTeamURL) localObject1).unwrap();
                else if ((localObject1 instanceof StatusMethod))
                    localObject2 = ((StatusMethod) localObject1).unwrap();
                else if ((localObject1 instanceof SupportedFeatures))
                    localObject2 = ((SupportedFeatures) localObject1).unwrap();
                else if ((localObject1 instanceof SystemPolicy))
                    localObject2 = ((SystemPolicy) localObject1).unwrap();
                else if ((localObject1 instanceof TaskDependency))
                    localObject2 = ((TaskDependency) localObject1).unwrap();
                else if ((localObject1 instanceof TimeSpanPropertyWrapper))
                    localObject2 = ((TimeSpanPropertyWrapper) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof Translations))
                    localObject2 = ((Translations) localObject1).unwrap();
                else if ((localObject1 instanceof TypeEvent))
                    localObject2 = ((TypeEvent) localObject1).unwrap();
                else if ((localObject1 instanceof TypeNotFoundException))
                    localObject2 = ((TypeNotFoundException) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof User))
                    localObject2 = ((User) localObject1).unwrap();
                else if ((localObject1 instanceof UserAccount))
                    localObject2 = ((UserAccount) localObject1).unwrap();
                else if ((localObject1 instanceof UserEvent))
                    localObject2 = ((UserEvent) localObject1).unwrap();
                else if ((localObject1 instanceof UserGroupSecLogID))
                    localObject2 = ((UserGroupSecLogID) localObject1).unwrap();
                else if ((localObject1 instanceof ViewConfiguration))
                    localObject2 = ((ViewConfiguration) localObject1).unwrap();
                else if ((localObject1 instanceof ViewConfigurationDiffer))
                    localObject2 = ((ViewConfigurationDiffer) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof ViewEvent))
                    localObject2 = ((ViewEvent) localObject1).unwrap();
                else if ((localObject1 instanceof ViewPollingAgent))
                    localObject2 = ((ViewPollingAgent) localObject1).unwrap();
                else if ((localObject1 instanceof WorkRecord))
                    localObject2 = ((WorkRecord) localObject1).unwrap();
                else if ((localObject1 instanceof Workstation))
                    localObject2 = ((Workstation) localObject1).unwrap();
                else if ((localObject1 instanceof EOLReader))
                    localObject2 = ((EOLReader) localObject1).unwrap();
                else if ((localObject1 instanceof FileAccess))
                    localObject2 = ((FileAccess) localObject1).unwrap();
                else if ((localObject1 instanceof FileBasedOptions))
                    localObject2 = ((FileBasedOptions) localObject1).unwrap();
                else if ((localObject1 instanceof GUID))
                    localObject2 = ((GUID) localObject1).unwrap();
                else if ((localObject1 instanceof LineProcessor))
                    localObject2 = ((LineProcessor) localObject1).unwrap();
                else if ((localObject1 instanceof LineReader))
                    localObject2 = ((LineReader) localObject1).unwrap();
                else if ((localObject1 instanceof MD5))
                    localObject2 = ((MD5) localObject1).unwrap();
                else if ((localObject1 instanceof MD5Stream))
                    localObject2 = ((MD5Stream) localObject1).unwrap();
                else if ((localObject1 instanceof OLEDate))
                    localObject2 = ((OLEDate) localObject1).unwrap();
                else if ((localObject1 instanceof OLEDateFormat))
                    localObject2 = ((OLEDateFormat) localObject1).unwrap();
                else if ((localObject1 instanceof StringCompare))
                    localObject2 = ((StringCompare) localObject1).unwrap();
                else if ((localObject1 instanceof TextInputStream))
                    localObject2 = ((TextInputStream) localObject1).unwrap();
                else if ((localObject1 instanceof UnixFileAccess))
                    localObject2 = ((UnixFileAccess) localObject1).unwrap();
                else if ((localObject1 instanceof UTF8InputStreamReader))
                    localObject2 = ((UTF8InputStreamReader) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof WintelOptions))
                    localObject2 = ((WintelOptions) localObject1).unwrap();
                else if ((localObject1 instanceof com.starbase.util.WorkStationException))
                    localObject2 = ((com.starbase.util.WorkStationException) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof XMLUtils))
                    localObject2 = ((XMLUtils) localObject1).unwrap();
                else if ((localObject1 instanceof Catalog))
                    localObject2 = ((Catalog) localObject1).unwrap();
                else if ((localObject1 instanceof Base64InputStream))
                    localObject2 = ((Base64InputStream) localObject1).unwrap();
                else if ((localObject1 instanceof BinaryChunkInputStream))
                    localObject2 = ((BinaryChunkInputStream) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof com.starbase.starteam.vts.comm.CommandException))
                    localObject2 = ((com.starbase.starteam.vts.comm.CommandException) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof com.starbase.starteam.vts.comm.ConnectionException))
                    localObject2 = ((com.starbase.starteam.vts.comm.ConnectionException) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof EncryptionAlgorithm))
                    localObject2 = ((EncryptionAlgorithm) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof ServerCommandEvent))
                    localObject2 = ((ServerCommandEvent) localObject1).unwrap();
                else if ((localObject1 instanceof BasicColumnMask))
                    localObject2 = ((BasicColumnMask) localObject1).unwrap();
                else if ((localObject1 instanceof BasicCompare))
                    localObject2 = ((BasicCompare) localObject1).unwrap();
                else if ((localObject1 instanceof BinaryCompare))
                    localObject2 = ((BinaryCompare) localObject1).unwrap();
                else if ((localObject1 instanceof ConsoleWriter))
                    localObject2 = ((ConsoleWriter) localObject1).unwrap();
                else if ((localObject1 instanceof DiffException))
                    localObject2 = ((DiffException) localObject1).unwrap();
                else if ((localObject1 instanceof Edit))
                    localObject2 = ((Edit) localObject1).unwrap();
                else if ((localObject1 instanceof EditList))
                    localObject2 = ((EditList) localObject1).unwrap();
                else if ((localObject1 instanceof EditListWriter))
                    localObject2 = ((EditListWriter) localObject1).unwrap();
                else if ((localObject1 instanceof HTMLDiffWriter))
                    localObject2 = ((HTMLDiffWriter) localObject1).unwrap();
                else if ((localObject1 instanceof HTMLInterleavedDiffWriter))
                    localObject2 = ((HTMLInterleavedDiffWriter) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof HTMLStereoDiffWriter))
                    localObject2 = ((HTMLStereoDiffWriter) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof IntralineSequence))
                    localObject2 = ((IntralineSequence) localObject1).unwrap();
                else if ((localObject1 instanceof LineIterator))
                    localObject2 = ((LineIterator) localObject1).unwrap();
                else if ((localObject1 instanceof ParsedCharSequence))
                    localObject2 = ((ParsedCharSequence) localObject1).unwrap();
                else if ((localObject1 instanceof SharedCharSequence))
                    localObject2 = ((SharedCharSequence) localObject1).unwrap();
                else if ((localObject1 instanceof StarTeamDiff))
                    localObject2 = ((StarTeamDiff) localObject1).unwrap();
                else if ((localObject1 instanceof StringCharSequence))
                    localObject2 = ((StringCharSequence) localObject1).unwrap();
                else if ((localObject1 instanceof Encoding))
                    localObject2 = ((Encoding) localObject1).unwrap();
                else if ((localObject1 instanceof XMLAttribute))
                    localObject2 = ((XMLAttribute) localObject1).unwrap();
                else if ((localObject1 instanceof XMLDocument))
                    localObject2 = ((XMLDocument) localObject1).unwrap();
                else if ((localObject1 instanceof XMLElement))
                    localObject2 = ((XMLElement) localObject1).unwrap();
                else if ((localObject1 instanceof XMLException))
                    localObject2 = ((XMLException) localObject1).unwrap();
                else if ((localObject1 instanceof ActionOverride))
                    localObject2 = ((ActionOverride) localObject1).unwrap();
                else if ((localObject1 instanceof com.starbase.starteam.viewcomparemerge.AmbiguousMatchException))
                    localObject2 = ((com.starbase.starteam.viewcomparemerge.AmbiguousMatchException) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof AnyScope))
                    localObject2 = ((AnyScope) localObject1).unwrap();
                else if ((localObject1 instanceof Custom3WayMergeAdapter))
                    localObject2 = ((Custom3WayMergeAdapter) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof DifferenceType))
                    localObject2 = ((DifferenceType) localObject1).unwrap();
                else if ((localObject1 instanceof EveryScope))
                    localObject2 = ((EveryScope) localObject1).unwrap();
                else if ((localObject1 instanceof FileMergeAdapter))
                    localObject2 = ((FileMergeAdapter) localObject1).unwrap();
                else if ((localObject1 instanceof com.starbase.starteam.viewcomparemerge.FileMergeException))
                    localObject2 = ((com.starbase.starteam.viewcomparemerge.FileMergeException) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof FolderScope))
                    localObject2 = ((FolderScope) localObject1).unwrap();
                else if ((localObject1 instanceof ItemDifference))
                    localObject2 = ((ItemDifference) localObject1).unwrap();
                else if ((localObject1 instanceof ItemListScope))
                    localObject2 = ((ItemListScope) localObject1).unwrap();
                else if ((localObject1 instanceof ItemTypeScope))
                    localObject2 = ((ItemTypeScope) localObject1).unwrap();
                else if ((localObject1 instanceof MatchState))
                    localObject2 = ((MatchState) localObject1).unwrap();
                else if ((localObject1 instanceof MergeAction))
                    localObject2 = ((MergeAction) localObject1).unwrap();
                else if ((localObject1 instanceof MergeType))
                    localObject2 = ((MergeType) localObject1).unwrap();
                else if ((localObject1 instanceof Options))
                    localObject2 = ((Options) localObject1).unwrap();
                else if ((localObject1 instanceof PreviewState))
                    localObject2 = ((PreviewState) localObject1).unwrap();
                else if ((localObject1 instanceof ProcessItemScope))
                    localObject2 = ((ProcessItemScope) localObject1).unwrap();
                else if ((localObject1 instanceof Progress))
                    localObject2 = ((Progress) localObject1).unwrap();
                else if ((localObject1 instanceof com.starbase.starteam.viewcomparemerge.PropertyMergeException))
                    localObject2 = ((com.starbase.starteam.viewcomparemerge.PropertyMergeException) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof RevisionLabelScope))
                    localObject2 = ((RevisionLabelScope) localObject1).unwrap();
                else if ((localObject1 instanceof Session))
                    localObject2 = ((Session) localObject1).unwrap();
                else if ((localObject1 instanceof Session))
                    localObject2 = ((Session) localObject1).unwrap();
                else if ((localObject1 instanceof SessionState))
                    localObject2 = ((SessionState) localObject1).unwrap();
                else if ((localObject1 instanceof StarTeamFileMergeAdapter))
                    localObject2 = ((StarTeamFileMergeAdapter) localObject1)
                            .unwrap();
                else if ((localObject1 instanceof VisMergeAdapter))
                    localObject2 = ((VisMergeAdapter) localObject1).unwrap();
                else if ((localObject1 instanceof WorkstationDefaultMergeAdapter))
                    localObject2 = ((WorkstationDefaultMergeAdapter) localObject1)
                            .unwrap();
            } else {
                Method localMethod = (Method) m_methods.get(paramClass1);
                if (localMethod != null)
                    try {
                        localObject2 = localMethod.invoke(localObject1,
                                new Object[0]);
                    } catch (Exception localException2) {
                        Assert.internalError(localException2.getMessage());
                    }
                Class localClass = paramClass1;
                while (localMethod == null)
                    try {
                        localMethod = localClass.getDeclaredMethod("unwrap",
                                new Class[0]);
                        localObject2 = localMethod.invoke(localObject1,
                                new Object[0]);
                        m_methods.put(localClass, localMethod);
                    } catch (NoSuchMethodException localNoSuchMethodException) {
                        localClass = localClass.getSuperclass();
                    } catch (Exception localException3) {
                        Assert.internalError(localException3.getMessage());
                    }
            }
            if ((localObject2.equals(paramObject))
                    && (localObject2 != paramObject)) {
                localWeakHashMap.remove(paramObject);
                localObject1 = null;
            }
        }
        if (localObject1 == null) {
            try {
                if (paramClass1.getName().equals(
                        "com.starbase.starteam.viewcomparemerge.MergePreview")) {
                    localObject1 = _PrivateMthds_
                            .wrapMergePreview((com.borland.starteam.impl.viewcomparemerge.MergePreview) paramObject);
                } else if (paramClass1.getName().equals(
                        "com.starbase.starteam.viewcomparemerge.VCMFolder")) {
                    localObject1 = _PrivateMthds_
                            .wrapVCMFolder((com.borland.starteam.impl.viewcomparemerge.VCMFolder) paramObject);
                } else {
                    localObject2 = (Constructor) m_constructors
                            .get(paramClass1);
                    if (localObject2 == null) {
                        localObject2 = paramClass1
                                .getDeclaredConstructor(new Class[] { paramClass2 });
                        m_constructors.put(paramClass1, localObject2);
                    }
                    localObject1 = ((Constructor) localObject2)
                            .newInstance(new Object[] { paramObject });
                }
            } catch (Exception localException1) {
                Assert.internalError(localException1.getMessage());
            }
            localWeakHashMap.put(paramObject, new WeakReference(localObject1));
        }
        return localObject1;
    }

    public com.borland.starteam.impl.Server unwrap() {
        return this.m_wrap;
    }

    private Server(com.borland.starteam.impl.Server paramServer) {
        this.m_wrap = paramServer;
    }

    public static Server wrap(com.borland.starteam.impl.Server paramServer) {
        return paramServer == null ? null : new Server(paramServer);
    }

    public Server(String paramString, int paramInt) {
        try {
            this.m_wrap = new com.borland.starteam.impl.Server(paramString,
                    paramInt);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public Server(String paramString, int paramInt,
            EncryptionAlgorithm paramEncryptionAlgorithm, boolean paramBoolean) {
        try {
            com.borland.starteam.impl.ServerInfo localServerInfo = new com.borland.starteam.impl.ServerInfo();
            localServerInfo.setHost(paramString);
            localServerInfo.setPort(paramInt);
            localServerInfo
                    .setEncryption(paramEncryptionAlgorithm == null ? null
                            : paramEncryptionAlgorithm.unwrap());
            localServerInfo.setCompression(paramBoolean);
            this.m_wrap = new com.borland.starteam.impl.Server(localServerInfo);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public Server(String paramString, int paramInt1,
            EncryptionAlgorithm paramEncryptionAlgorithm, boolean paramBoolean,
            int paramInt2) {
        try {
            com.borland.starteam.impl.ServerInfo localServerInfo = new com.borland.starteam.impl.ServerInfo();
            localServerInfo.setHost(paramString);
            localServerInfo.setPort(paramInt1);
            localServerInfo
                    .setEncryption(paramEncryptionAlgorithm == null ? null
                            : paramEncryptionAlgorithm.unwrap());
            localServerInfo.setCompression(paramBoolean);
            localServerInfo.setConnectionType(paramInt2);
            this.m_wrap = new com.borland.starteam.impl.Server(localServerInfo);
            this.m_wrap = new com.borland.starteam.impl.Server(localServerInfo);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public Server(ServerInfo paramServerInfo) {
        this.m_wrap = new com.borland.starteam.impl.Server(
                paramServerInfo == null ? null : paramServerInfo.unwrap());
    }

    public ServerAdministration getAdministration() {
        try {
            return ServerAdministration.wrap(unwrap().getAdministration());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public Link[] findLinks(Item paramItem) {
        try {
            return Link.wrap(unwrap().findLinks(
                    paramItem == null ? null
                            : (com.borland.starteam.impl.Item) paramItem
                                    .unwrap()));
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void setCommandUserID(int paramInt) {
        try {
            unwrap().setCommandUserID(paramInt);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean equals(Object paramObject) {
        try {
            if ((paramObject instanceof Server))
                return unwrap().equals(((Server) paramObject).unwrap());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
        return false;
    }

    public int hashCode() {
        try {
            return unwrap().hashCode();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean isRefreshTypesRequired() {
        try {
            return unwrap().isRefreshTypesRequired();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void refreshTypes() {
        try {
            unwrap().refreshTypes();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void refreshTypesInPlace() {
        try {
            unwrap().refreshTypes();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void discardTypes() {
        try {
            unwrap().discardTypes();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public MyUserAccount getMyUserAccount() {
        try {
            return MyUserAccount.wrap(unwrap().getMyUserAccount());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public ServerSession getSession() {
        try {
            return new Session(unwrap().getSession());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public User[] fetchEmailUsers() {
        try {
            return User.wrap(unwrap().fetchEmailUsers());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean isRefreshUsersRequired() {
        try {
            return unwrap().isRefreshUsersRequired();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void refreshUsers() {
        try {
            unwrap().refreshUsers();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void discardUsers() {
        try {
            unwrap().discardUsers();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void refreshAccounts() {
        try {
            unwrap().refreshAccounts();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void discardAccounts() {
        try {
            unwrap().discardAccounts();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean isRefreshAccountsRequired() {
        try {
            return unwrap().isRefreshAccountsRequired();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean isRefreshGroupsRequired() {
        try {
            return unwrap().isRefreshGroupsRequired();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void discardGroups() {
        try {
            unwrap().discardGroups();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void refreshGroups() {
        try {
            unwrap().refreshGroups();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public String getAddress() {
        try {
            return unwrap().getAddress();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public int getPort() {
        try {
            return unwrap().getPort();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public EncryptionAlgorithm getEncryptionAlgorithm() {
        try {
            return EncryptionAlgorithm.wrap(unwrap().getEncryptionAlgorithm());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean isCompressed() {
        try {
            return unwrap().isCompressed();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public int getProtocol() {
        try {
            return unwrap().getProtocol();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public GUID getRepositoryID() {
        try {
            return GUID.wrap(unwrap().getRepositoryID());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public EncryptionAlgorithm getRequiredEncryptionLevel() {
        try {
            return EncryptionAlgorithm.wrap(unwrap()
                    .getRequiredEncryptionLevel());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean isConnected() {
        try {
            return unwrap().isConnected();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public Connection useConnection() {
        throw new UnsupportedOperationException();
    }

    public ClientContext getClientContext() {
        try {
            return ClientContext.wrap(unwrap().getClientContext());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public Project[] getProjects() {
        try {
            return Project.wrap(_PrivateMethods_
                    .refreshAndCopyProjects(unwrap()));
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean isTypeSupported(String paramString) {
        try {
            return unwrap().isTypeSupported(paramString);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public Type[] getTypes() {
        try {
            return Type.wrap(unwrap().getTypes());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public Type typeForName(String paramString)
            throws IllegalArgumentException, TypeNotFoundException {
        try {
            return Type.wrap(unwrap().typeForName(paramString));
        } catch (com.borland.starteam.impl.TypeNotFoundException localTypeNotFoundException) {
            throw TypeNotFoundException.wrap(localTypeNotFoundException);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public Type typeForClassID(int paramInt) throws TypeNotFoundException {
        try {
            return Type.wrap(unwrap().typeForClassID(paramInt));
        } catch (com.borland.starteam.impl.TypeNotFoundException localTypeNotFoundException) {
            throw TypeNotFoundException.wrap(localTypeNotFoundException);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public User[] getActiveUsers() {
        try {
            return User.wrap(unwrap().getActiveUsers());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public User[] getUsers() {
        try {
            return User.wrap(unwrap().getUsers());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public Group[] getActiveGroups() {
        try {
            return Group.wrap(unwrap().getActiveGroups());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public Group[] getGroups() {
        try {
            return Group.wrap(unwrap().getGroups());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public User getUser(int paramInt) {
        try {
            return User.wrap(unwrap().getUser(paramInt));
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public Group getGroup(int paramInt) {
        try {
            return Group.wrap(unwrap().getGroup(paramInt));
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public SupportedFeatures getSupportedFeatures() {
        try {
            return SupportedFeatures.wrap(unwrap().getSupportedFeatures());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean hasProductCodeInformation() {
        return true;
    }

    public int[] getProductCodes() {
        try {
            return unwrap().getProductCodes();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean hasProductCode(int paramInt) {
        try {
            return unwrap().hasProductCode(paramInt);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public short getRevisionLevel() {
        try {
            return unwrap().getRevisionLevel();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public String getCommandAPIRevisionLevel() {
        try {
            return unwrap().getCommandAPIRevisionLevel();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public String getServerBuild() {
        try {
            return unwrap().getServerBuild();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public String getServerBuildDescription() {
        try {
            return unwrap().getServerBuildDescription();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public PropertyNames getPropertyNames() {
        try {
            return new PropertyNames();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public PropertyEnums getPropertyEnums() {
        try {
            return new PropertyEnums();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public TypeNames getTypeNames() {
        try {
            return new TypeNames();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public int getCommandCount() {
        try {
            return unwrap().getCommandCount();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public int getMPXMessageCount() {
        try {
            return unwrap().getMPXMessageCount();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void connect() {
        try {
            unwrap().connect();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void disconnect() {
        try {
            m_servers.remove(this);
            unwrap().disconnect();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public int logOn(String paramString1, String paramString2) {
        try {
            return unwrap().logOn(paramString1, paramString2);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void changePassword(String paramString1, String paramString2,
            String paramString3) {
        try {
            unwrap().changePassword(paramString1, paramString2, paramString3);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public int logOnForWorkstation(String paramString1, String paramString2,
            GUID paramGUID) {
        try {
            return unwrap().logOnForWorkstation(paramString1, paramString2,
                    paramGUID == null ? null : paramGUID.unwrap());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public int logOnWithClientContext(String paramString1, String paramString2,
            ClientContext paramClientContext) {
        try {
            return unwrap().logOnWithClientContext(
                    paramString1,
                    paramString2,
                    paramClientContext == null ? null : paramClientContext
                            .unwrap());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void reconnect() {
        try {
            unwrap().reconnect();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void reconnect(String paramString) {
        try {
            unwrap().reconnect(paramString);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean isAutoReconnectEnabled() {
        try {
            return unwrap().isAutoReconnectEnabled();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void setAutoReconnectEnabled(boolean paramBoolean) {
        try {
            unwrap().setAutoReconnectEnabled(paramBoolean);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public int getAutoReconnectAttempts() {
        try {
            return unwrap().getAutoReconnectAttempts();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void setAutoReconnectAttempts(int paramInt) {
        try {
            unwrap().setAutoReconnectAttempts(paramInt);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public int getAutoReconnectWait() {
        try {
            return unwrap().getAutoReconnectWait();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void setAutoReconnectWait(int paramInt) {
        try {
            unwrap().setAutoReconnectWait(paramInt);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean ping() {
        try {
            return unwrap().ping();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public OLEDate getCurrentTime() {
        try {
            return OLEDate.wrap(unwrap().getCurrentTime());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public static boolean isCredentialCachingAvailable() {
        try {
            return com.borland.starteam.impl.Server
                    .isCredentialCachingAvailable();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public static boolean isCredentialCachingEnabled() {
        try {
            return com.borland.starteam.impl.Server
                    .isCredentialCachingEnabled();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public static void enableCredentialCaching() {
        try {
            com.borland.starteam.impl.Server.enableCredentialCaching();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean isAutoLogOnAvailable() {
        try {
            return unwrap().isAutoLogOnAvailable();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public int autoLogOn() {
        try {
            return unwrap().autoLogOn();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void cacheLogOnCredentials(String paramString1, String paramString2) {
        try {
            unwrap().cacheLogOnCredentials(paramString1, paramString2);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public String toString() {
        try {
            return unwrap().toString();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean isLoggedOn() {
        try {
            return unwrap().isLoggedOn();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void sendMailFromView(View paramView, int[] paramArrayOfInt1,
            int[] paramArrayOfInt2, int[] paramArrayOfInt3,
            String paramString1, String paramString2) {
        try {
            unwrap().sendMailFromView(
                    paramView == null ? null
                            : (com.borland.starteam.impl.View) paramView
                                    .unwrap(),
                    paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3,
                    paramString1, paramString2);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void sendMailWithAttachments(View paramView, int[] paramArrayOfInt1,
            int[] paramArrayOfInt2, int[] paramArrayOfInt3,
            String paramString1, String paramString2,
            EmailAttachment[] paramArrayOfEmailAttachment) {
        try {
            unwrap().sendMailWithAttachments(
                    paramView == null ? null
                            : (com.borland.starteam.impl.View) paramView
                                    .unwrap(),
                    paramArrayOfInt1,
                    paramArrayOfInt2,
                    paramArrayOfInt3,
                    paramString1,
                    paramString2,
                    paramArrayOfEmailAttachment == null ? null
                            : EmailAttachment
                                    .unwrap(paramArrayOfEmailAttachment));
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public Filter[] getFilterList(Type paramType, boolean paramBoolean) {
        try {
            return Filter.wrap(unwrap()
                    .getFilterList(
                            paramType == null ? null : paramType.unwrap(),
                            paramBoolean));
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public QueryInfo[] getQueryList(Type paramType, boolean paramBoolean) {
        try {
            return QueryInfo.wrap(unwrap()
                    .getQueryList(
                            paramType == null ? null : paramType.unwrap(),
                            paramBoolean));
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void setForeignPassword(Project paramProject) {
        try {
            unwrap().setForeignPassword(
                    paramProject == null ? null
                            : (com.borland.starteam.impl.Project) paramProject
                                    .unwrap());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void setForeignPasswordExplicit(String paramString1, int paramInt,
            String paramString2) {
        try {
            unwrap().setForeignPasswordExplicit(paramString1, paramInt,
                    paramString2);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean isMailAvailable() {
        try {
            return unwrap().isMailAvailable();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public String[] getEventHandlerInfoNames() {
        try {
            boolean bool = unwrap().getSupportedFeatures()
                    .hasMPXTransmitterNameChanged();
            String[] arrayOfString1 = unwrap().getEventHandlerInfoNames();
            if (arrayOfString1.length == 0)
                return arrayOfString1;
            int i = 0;
            String str = "StarbaseMPX Transmitter";
            for (int j = 0; j < arrayOfString1.length; j++)
                if (arrayOfString1[j].equals(str))
                    i = 1;
            String[] arrayOfString2 = null;
            if (i != 0)
                arrayOfString2 = new String[arrayOfString1.length];
            else
                arrayOfString2 = new String[arrayOfString1.length + 1];
            if (bool) {
                for (int k = 0; k < arrayOfString1.length; k++)
                    arrayOfString2[k] = arrayOfString1[k];
                if (i == 0)
                    arrayOfString2[(arrayOfString2.length - 1)] = str;
                return arrayOfString2;
            }
            return arrayOfString1;
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public EventHandlerInfo getEventHandlerInfo(String paramString) {
        if ((paramString.equals("StarbaseMPX Transmitter"))
                && (!paramString.equals(getMPXTransmitterName())))
            paramString = getMPXTransmitterName();
        try {
            return EventHandlerInfo.wrap(unwrap().getEventHandlerInfo(
                    paramString));
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean isMPXAvailable() {
        try {
            return unwrap().isMPXAvailable();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void enableMPX() throws MPXException {
        try {
            unwrap().enableMPX();
        } catch (com.borland.starteam.impl.MPXException localMPXException) {
            throw MPXException.wrap(localMPXException);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void enableMPX(String paramString) throws MPXException {
        try {
            unwrap().enableMPX(paramString);
        } catch (com.borland.starteam.impl.MPXException localMPXException) {
            throw MPXException.wrap(localMPXException);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void enableMPX(EventHandlerProfile paramEventHandlerProfile)
            throws MPXException {
        try {
            unwrap().enableMPX(
                    paramEventHandlerProfile == null ? null
                            : paramEventHandlerProfile.unwrap());
        } catch (com.borland.starteam.impl.MPXException localMPXException) {
            throw MPXException.wrap(localMPXException);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public String getMPXTransmitterName() {
        return unwrap().getMPXTransmitterName();
    }

    public boolean isMPXEnabled() {
        try {
            return unwrap().isMPXEnabled();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public EventHandlerProfile getCurrentMPXProfile() {
        try {
            return EventHandlerProfile.wrap(unwrap().getCurrentMPXProfile());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean isMPXResponding() {
        try {
            return unwrap().isMPXResponding();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void pingMPXServer() throws MPXException {
        try {
            unwrap().pingMPXServer();
        } catch (com.borland.starteam.impl.MPXException localMPXException) {
            throw MPXException.wrap(localMPXException);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void disableMPX() {
        try {
            unwrap().disableMPX();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void handleEvents() {
        try {
            unwrap().handleEvents();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void interruptHandleEvents() {
        try {
            unwrap().interruptHandleEvents();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void addMPXStatusListener(MPXStatusListener paramMPXStatusListener) {
        try {
            if (this.m_listeners.containsKey(paramMPXStatusListener))
                return;
            MPXStatusListenerImpl localMPXStatusListenerImpl = new MPXStatusListenerImpl(
                    paramMPXStatusListener);
            this.m_listeners.put(paramMPXStatusListener,
                    localMPXStatusListenerImpl);
            unwrap().addMPXStatusListener(localMPXStatusListenerImpl);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void removeMPXStatusListener(MPXStatusListener paramMPXStatusListener) {
        try {
            MPXStatusListenerImpl localMPXStatusListenerImpl = null;
            if (this.m_listeners.containsKey(paramMPXStatusListener))
                localMPXStatusListenerImpl = (MPXStatusListenerImpl) this.m_listeners
                        .remove(paramMPXStatusListener);
            unwrap().removeMPXStatusListener(localMPXStatusListenerImpl);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void addServerControlListener(
            ServerControlListener paramServerControlListener) {
        try {
            if (this.m_listeners.containsKey(paramServerControlListener))
                return;
            ServerControlListenerImpl localServerControlListenerImpl = new ServerControlListenerImpl(
                    paramServerControlListener);
            this.m_listeners.put(paramServerControlListener,
                    localServerControlListenerImpl);
            unwrap().addServerControlListener(localServerControlListenerImpl);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void removeServerControlListener(
            ServerControlListener paramServerControlListener) {
        try {
            ServerControlListenerImpl localServerControlListenerImpl = null;
            if (this.m_listeners.containsKey(paramServerControlListener))
                localServerControlListenerImpl = (ServerControlListenerImpl) this.m_listeners
                        .remove(paramServerControlListener);
            unwrap().removeServerControlListener(localServerControlListenerImpl);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void addTypeListener(TypeListener paramTypeListener) {
        try {
            if (this.m_listeners.containsKey(paramTypeListener))
                return;
            TypeListenerImpl localTypeListenerImpl = new TypeListenerImpl(
                    paramTypeListener);
            this.m_listeners.put(paramTypeListener, localTypeListenerImpl);
            unwrap().addTypeListener(localTypeListenerImpl);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void removeTypeListener(TypeListener paramTypeListener) {
        try {
            TypeListenerImpl localTypeListenerImpl = null;
            if (this.m_listeners.containsKey(paramTypeListener))
                localTypeListenerImpl = (TypeListenerImpl) this.m_listeners
                        .remove(paramTypeListener);
            unwrap().removeTypeListener(localTypeListenerImpl);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void addUserListener(UserListener paramUserListener) {
        try {
            if (this.m_listeners.containsKey(paramUserListener))
                return;
            UserListenerImpl localUserListenerImpl = new UserListenerImpl(
                    paramUserListener);
            this.m_listeners.put(paramUserListener, localUserListenerImpl);
            unwrap().addUserListener(localUserListenerImpl);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void removeUserListener(UserListener paramUserListener) {
        try {
            UserListenerImpl localUserListenerImpl = null;
            if (this.m_listeners.containsKey(paramUserListener))
                localUserListenerImpl = (UserListenerImpl) this.m_listeners
                        .remove(paramUserListener);
            unwrap().removeUserListener(localUserListenerImpl);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void addProjectListener(ProjectListener paramProjectListener) {
        try {
            if (this.m_listeners.containsKey(paramProjectListener))
                return;
            ProjectListenerImpl localProjectListenerImpl = new ProjectListenerImpl(
                    paramProjectListener);
            this.m_listeners
                    .put(paramProjectListener, localProjectListenerImpl);
            unwrap().addProjectListener(localProjectListenerImpl);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void removeProjectListener(ProjectListener paramProjectListener) {
        try {
            ProjectListenerImpl localProjectListenerImpl = null;
            if (this.m_listeners.containsKey(paramProjectListener))
                localProjectListenerImpl = (ProjectListenerImpl) this.m_listeners
                        .remove(paramProjectListener);
            unwrap().removeProjectListener(localProjectListenerImpl);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public int getKeepAliveInterval() {
        try {
            return unwrap().getKeepAliveInterval();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void setKeepAliveInterval(int paramInt) {
        try {
            unwrap().setKeepAliveInterval(paramInt);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public boolean isKeepAlive() {
        try {
            return unwrap().isKeepAlive();
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public synchronized void setKeepAlive(boolean paramBoolean) {
        try {
            unwrap().setKeepAlive(paramBoolean);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public String[] getForeignArchivePaths(int paramInt, String paramString) {
        try {
            return unwrap().getForeignArchivePaths(paramInt, paramString);
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public CacheAgent autoLocateCacheAgent() {
        try {
            return CacheAgent.wrap(unwrap().autoLocateCacheAgent());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public CacheAgent locateCacheAgent(String paramString, int paramInt) {
        try {
            return CacheAgent.wrap(unwrap().locateCacheAgent(paramString,
                    paramInt));
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public CacheAgent getCurrentCacheAgent() {
        try {
            return CacheAgent.wrap(unwrap().getCurrentCacheAgent());
        } catch (RuntimeException localRuntimeException) {
            throw wrap(localRuntimeException);
        }
    }

    public void setMPXCacheAgentEnabled(boolean paramBoolean) {
        unwrap().setMPXCacheAgentEnabled(paramBoolean);
    }

    public boolean getMPXCacheAgentEnabled() {
        return unwrap().getMPXCacheAgentEnabled();
    }

    static class Session implements ServerSession {
        protected com.borland.starteam.impl.ServerSession m_wrap = null;

        com.borland.starteam.impl.ServerSession unwrap() {
            return this.m_wrap;
        }

        public Session(
                com.borland.starteam.impl.ServerSession paramServerSession) {
            this.m_wrap = paramServerSession;
        }

        public GUID getID() {
            try {
                return GUID.wrap(unwrap().getID());
            } catch (RuntimeException localRuntimeException) {
                throw Server.wrap(localRuntimeException);
            }
        }

        public int getUserID() {
            try {
                return unwrap().getUserID();
            } catch (RuntimeException localRuntimeException) {
                throw Server.wrap(localRuntimeException);
            }
        }

        public String toString() {
            try {
                return unwrap().toString();
            } catch (RuntimeException localRuntimeException) {
                throw Server.wrap(localRuntimeException);
            }
        }
    }
}