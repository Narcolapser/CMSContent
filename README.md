#University of South Dakota External Content Management Portlet

This portlet is a re-write of the portlet USD uses to draw content in from an
external content management system. The original portlet had 3 seperate code
bases and was very rigid in that it had no way of changing where it drew its
content from, the portlets were not able to be managed by anyone other than 
portal admins, and changing the portlet type required deleting the portlet and
making a new portlet. This re-write seeks to solve those issues by:

1. Generalizing the data model. Allowing for other resources to be used.
2. Making the portlets configurable.
3. Putting the configuration page inside the portlet.


##Road map

###0.3:

* Add tabbed view. - check
* Seperate the view out into seperate jsp files for modularity. - check

###0.4:

* Allow for selection of data source in individual portlet/channel configuration. - check
* Add internal CMS - check
* Add internal Editor - Check

###0.5

* Change content if view is maximized. - Check
* Recreate config interface - check
* Refactor views into classes to allow view specific properties and settings. check
  * Refine Vertical Tabs - Check
  * Refine Tabs - Check

###0.6:

* Integrate security into the portlet. Allow who sees what to be configured per document.
* Get properties to save and load. - Check
* More layouts:
  * Vertical Tabs with right side pane
  * Horizontal Tabs with right side pane
  * Horizontal Tabs with left side Pane
  * Vertical tabs on right
