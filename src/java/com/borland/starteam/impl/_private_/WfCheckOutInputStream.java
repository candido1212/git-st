package com.borland.starteam.impl._private_;

import com.borland.starteam.impl.File;
import com.borland.starteam.impl.FileSyncInfo;
import com.borland.starteam.impl.Folder;
import com.borland.starteam.impl.MakeVisible;
import com.borland.starteam.impl.Project;
import com.borland.starteam.impl.Server;
import com.borland.starteam.impl.View;
import com.borland.starteam.impl._private_.vts.pickle.FileRevisionID;
import com.borland.starteam.impl._private_.vts.pickle.ViewMemberConfig;
import com.borland.starteam.impl.util.MD5;
import java.io.IOException;
import java.io.InputStream;

public class WfCheckOutInputStream extends InputStream {
	private WfCmdCheckOutInputStream m_Cmd = null;
	
	public WfCheckOutInputStream(Server server, int itemId, int componentId, int viewSessionId, boolean deltasOK, ViewMemberConfig vmconfig, FileSyncInfo syncInfo, MD5 localMD5, boolean bVerifyStatus, boolean bLocalMods) throws IOException {
		this.m_Cmd = new WfCmdCheckOutInputStream(server, itemId, componentId, viewSessionId, deltasOK, vmconfig, syncInfo, localMD5, bVerifyStatus, bLocalMods);
	}
	
	public WfCheckOutInputStream(com.starbase.starteam.Server server, int itemId, int componentId, int viewSessionId, boolean deltasOK, ViewMemberConfig vmconfig, FileSyncInfo syncInfo, MD5 localMD5, boolean bVerifyStatus, boolean bLocalMods) throws IOException {
		this.m_Cmd = new WfCmdCheckOutInputStream((Server)server.unwrap(), itemId, componentId, viewSessionId, deltasOK, vmconfig, syncInfo, localMD5, bVerifyStatus, bLocalMods);
	}
	
	public static int getViewSessionId(View view) {
		return MakeVisible.classServer_getViewSession(view.getServer(), view).getID();
	}
	
	public static int getComponentId(File file) {
		return MakeVisible.classFile_getRoutingComponentID(file);
	}

	public void close() throws IOException {
		this.m_Cmd.finish();
		super.close();
	}

	public double getFileTime() {
		return this.m_Cmd.getFileTime();
	}

	public boolean getFileExecutable() {
		return this.m_Cmd.getFileExecutable();
	}

	public MD5 getSyncMD5() {
		return this.m_Cmd.getSyncMD5();
	}

	int getExclusiveLockerID() {
		return this.m_Cmd.getExclusiveLockerID();
	}

	int getMyLockState() {
		return this.m_Cmd.getMyLockState();
	}

	public FileRevisionID getFileRevisionID() {
		return this.m_Cmd.getFileRevisionID();
	}

	public WfCmdCheckOutInputStream getCommand() {
		return this.m_Cmd;
	}

	public int read() throws IOException {
		return this.m_Cmd.read();
	}

	public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
		return this.m_Cmd.read(paramArrayOfByte, paramInt1, paramInt2);
	}
}
