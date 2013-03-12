package com.aap.gitst;

import com.starbase.util.OLEDate;

/**
 * Identifies a view in the cache
 * 
 * @author Warren Falk <warren@warrenfalk.com>
 * 
 */
public class ViewCacheKey implements Comparable<ViewCacheKey> {
    final int viewId;
    final double asOf;

    public ViewCacheKey(final int viewId, final double asOf) {
        this.viewId = viewId;
        this.asOf = asOf;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ViewCacheKey)
            return equals((ViewCacheKey) obj);
        else
            return false;
    }

    public boolean equals(ViewCacheKey other) {
        return other.viewId == this.viewId && other.asOf == this.asOf;
    }

    @Override
    public int hashCode() {
        return Double.valueOf(asOf).hashCode() ^ viewId;
    }
    
    @Override
    public String toString() {
        return "#" + viewId + "@" + new OLEDate(asOf);
    }

    @Override
    public int compareTo(ViewCacheKey o) {
        int c = Integer.compare(this.viewId, o.viewId);
        if (c != 0)
            return c;
        c = Double.compare(this.asOf, o.asOf);
        return c;
    }
}
