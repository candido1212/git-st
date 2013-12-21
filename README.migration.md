StarTeam to Git Migration
=========================

It is currently possible to use the git remote helper for StarTeam as a tool to migrate a project's history from StarTeam to git, but currently a little care needs to be taken.

Note: these instructions are for those who want to do a one-time migration to git.  If you want to use git as a front-end for StarTeam, this document is not for you.  See the main README.

The following environment variables must be set:

    export GITST_DEBUG=true
    export GITST_SKIP_EMPTY_DIRS=true
    export GITST_SKIP_DELETED=false

You can then clone a starteam repository using something like this:

    git clone starteam://user:password@server:port/Project/TopView/ChildView/GrandchildView

Git will create a new folder, GrandchildView, and the git-st importer will import all the history from that view, its parent view (ChildView), and so on.
It currently does not import history from TopView.
If this is incorrect, there is a FIXME where you can modify the code to properly select your desired root view.
It will not import siblings of or children of GrandchildView, but it is possible to pull from other views by adding them as remotes.
This works, in general, but will not record merge lines (merges will just appear as regular commits with single parents, but all content will be correct)

It is theoretically possible to manually apply merge lines, but that is outside the scope of this document.
