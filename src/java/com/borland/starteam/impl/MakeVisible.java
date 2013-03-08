package com.borland.starteam.impl;

import com.borland.starteam.impl._private_.StatusDataImpl;
import com.borland.starteam.impl._private_.ViewSession;
import com.borland.starteam.impl._private_.vts.comm.Connection;
import com.borland.starteam.impl._private_.vts.comm.NetMonitorCore;

public class MakeVisible {

	public static boolean classFile_getUsesKeywords(File m_file, CheckoutOptions options) {
		return m_file.getUsesKeywords(options);
	}

	public static Connection classServer_useConnection(Server localServer) {
		return localServer.useConnection();
	}

	public static CheckOutResult classServer_newCheckOutResult(Server server, Keyword[] arrayOfKeyword, int exclusiveLockerID, int myLockState) {
		return server.newCheckOutResult(arrayOfKeyword, exclusiveLockerID, myLockState);
	}

	public static void classServer_cacheStatusData(Server server, View view, String paramString, StatusDataImpl paramStatusDataImpl) {
		server.cacheStatusData(view, paramString, paramStatusDataImpl);
	}

	public static void classFile_removeCachedServerCalculatedProperty(File m_file, Property localProperty2) {
		m_file.removeCachedServerCalculatedProperty(localProperty2);
	}

	public static FileSyncInfo classFile_getStatusData(File m_file) {
		return m_file.getStatusData();
	}

	public static View[] classFile_getViews(File m_file) {
		return m_file.getViews();
	}

	public static ViewSession classServer_getViewSession(Server server, View view) {
		return server.getViewSession(view);
	}

	public static ViewSession classServer_getViewSession(com.starbase.starteam.Server server, com.starbase.starteam.View view) {
		return ((Server)server.unwrap()).getViewSession((View)view.unwrap());
	}

	public static int classFile_getRoutingComponentID(File file) {
		return file.getRoutingComponentID();
	}

	public static int classFile_getRoutingComponentID(com.starbase.starteam.File file) {
		return ((File)file.unwrap()).getRoutingComponentID();
	}

	public static boolean classCheckoutManager_onNotifyProgress(CheckoutManager checkoutManager, CheckoutEvent m_checkoutEvent) {
		return checkoutManager.onNotifyProgress(m_checkoutEvent);
	}

	public static boolean classCheckoutManager_onCheckoutStart(CheckoutManager checkoutManager, CheckoutEvent m_checkoutEvent) {
		return checkoutManager.onCheckoutStart(m_checkoutEvent);
	}

	public static boolean classCheckoutManager_onCheckoutError(CheckoutManager checkoutManager, CheckoutEvent m_checkoutEvent) {
		return checkoutManager.onCheckoutError(m_checkoutEvent);
	}

	public static NetMonitorCore classServer_newNetMonitor(Server paramServer) {
		return paramServer.newNetMonitor();
	}

}
