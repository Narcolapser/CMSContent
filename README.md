#University of South Dakota External Content Management Portlet

This portlet is a re-write of the portlet USD uses to draw content in from an
external content management system. The original portlet had 3 seperate code
bases and was very rigid in that it had no way of changing where it drew it's
content from, the portlets were not able to be managed by anyone other than 
portal admins, and changing the portlet type required deleting the portlet and
making a new portlet. This re-write seeks to solve those issues by:

1. Generalizing the data model. Allowing for other resources to be used.
2. Making the portlets configurable.
3. Putting the configuration page inside the portlet.


##Road map

###0.3:

* Add tabbed view.
* Seperate the view out into seperate jsp files for modularity.

###0.4:

* Integrate security into the portlet. Allow who sees what to be configured per page.
* Allow for selection of data source in individual portlet configuration.
