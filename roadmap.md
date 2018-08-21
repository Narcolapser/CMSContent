## Road map

### 0.3:

* Add tabbed view. - check
* Seperate the view out into seperate jsp files for modularity. - check

### 0.4:

* Allow for selection of data source in individual portlet/channel configuration. - check
* Add internal CMS - check
* Add internal Editor - Check

### 0.5

* Change content if view is maximized. - Check
* Recreate config interface - check
* Refactor views into classes to allow view specific properties and settings. check
  * Refine Vertical Tabs - Check
  * Refine Tabs - Check

### 0.6:

* Integrate security into the portlet. Allow who sees what to be configured per document. - check
* Get properties to save and load. - Check

### 0.7:

* Forms. - check
* CMS Editor fixes:
  * make update/save work on first click. - check
* Fix issue where re-arranged documents or new documents loose security config dialog. - check
* Add edit/new form button to config view - check

### 0.8:

* upload files - Check
* search integration - Check
	* per-document search terms - Check
* common spot portal page - Check
* Configurable responders for forms. - Check
* Move JS libraries to webjars. - Check

### 0.9:
* "delete" documents (actually just flagging them as deleted, never delete anything) - check
* CMSEditor tree updates when new document saved. - check
* setup with Lucene search library. - Failed
* Forms as a datasource - check
* JSPs in sub folders. - check
* CMSForm and CMSEditor using same layout for document saving. - check

### 1.0:
* portlet caching - Check
* Video play back
* CMSForms all bounce off of the database so that response time can be very fast. - Check

### 1.1:
* Unified CMSEditor.
* WebComponents to simplify Editors.
* CMSForm confirmation email needs to use a document for it's return body. But this will require a fancier way to set options.
* Version 1 of API Phased out. 


### Abandoned:
* More layouts:
  * Vertical Tabs with right side pane
  * Horizontal Tabs with right side pane
  * Horizontal Tabs with left side Pane
  * Vertical tabs on right

* Reports and internal forms don't index properly to be searched. -- search just wasn't quite working how I liked so this is abandoned for now. 

Query for copying prod table:
DROP TABLE [uPortal].[dbo].[CMSDocument]
SELECT * INTO [uPortal].[dbo].[CMSDocument] FROM [USD-SQL05\SQL01].uPortal.dbo.CMSDocument

## Other things I've noticed that need fixing:
* Vertical tabs kind of breaks on smaller displays, should probably turn into expanding on smaller displays.
* Compond keys with document id and document type.
* Reports don't show date time or user name.
* Vertical tab's content has a box around it. remove this.
* Saving without selecting a document in CMSForm causes the page to break.
* datetime picker that works across all browsers.

# Fixed things:
* Vertical tabs doesn't force content size effectively enough.
* Automaticly displaying source content is broken in CMSEditor.
* Return button is broken on CMSForm editor
* Horizontal tabs has zero clip instead of url rewriting.
* Deleted documents cause the display of a portlet to explode.
* Expanding layout gets stuck if you click "Collapse all"
* Responders are showing up in reports.
* CMSEditor displays internal forms and internal reports even though it can't edit them (properly)
* Double check all the input types in CMSForms, at least checkbox doesn't actually get saved if required
* No way to remove extra Responders.
* Provide a short cut to edit a document. 
* loading CMSForm does not load it's responder type.
* CMSEditor/CMSForm delete double check.
* disable required box on controls it doesn't make sense on. 
* Layout doc source should default to internal
* Document sources should have a seperate display name.
