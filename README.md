# External Content Management Portlet

This portlet started out as a re-write to allow for migrating content from one external CMS to another. That principle still sits at the heart of this portlet. While it is possible to host content from directly within the portal via this portlet, you are expected to write an adapter for your external CMS content provider. There are many other parts that are also extendable, see below for futher details.

## General Theory

The idea of this portlet is that there are "Documents" hosted from various "Document Sources". Each portlet has a normal view and maximized view (if no maximized view is defined, normal is used as the maximized view). Each view has a layout and a set of subscriptions. A subscription is a many to many relationship between layouts and documents. So you create a CMSContent portlet, go into it's configuration view, select a layout, and then subscribe to various documents from various document sources. Each subscription can also have security groups applied to it. These are defined in the portlet.xml file. If no group is provided then the document is visible to all. Multiple security groups are OR-ed together. 

# Extending CMSContent

## Custom Document Source

In `src/main/java/edu/usd/portlet/CMSContent/dao/CMSDocumentDao.java` you will find the interface you must implement in order for the portlet to pick up your document source. The interface will explain all of the methods needed. As long as you put your source in `package edu.usd.portlet.cmscontent.dao;` it should be picked up automatically and will show up in the portlets.

## Custom Document Type

Rarely you may need to provide a mechanism for displaying content in more than just a raw manner, such as personalizing the document. If your document source needs to do such tasks you may need to subclass the CMSDocument class and override the render method. This allows you greater control over the displayed content.

## Custom Layout

More layouts are also able to be added to provide more options in how the documents are to be displayed. The base class located at `src/main/java/edu/usd/portlet/CMSContent/dao/CMSLayout.java` has the basics you need to extend. You will have to write your own getName, getView, getDescription, and copy methods to wire everything up, as well as getDefaultProperties and getProperties if you need special properties for your new layout. Finally you'll need to provide a the custom jsp in the webapp folder. I encourage you to reverse engineer an existing layout like VerticalTabs.

## Custom Properties Store

While it is possible to create a property store that doesn't use the portlet's properties it is not recommended at this point. The portlet will need a way of uniquely identifying itself. The best way to do this is via fname which is not accesible from a render request, so I don't recommend this at this point. It may become a more viable option if this is turned into a soffit. 

# Forms and Reports

Out side of HTML documents there are two other document types that won't integrate with external sources. CMSForms and CMSReports. CMSForms are a way to put forms in the portal and save the results through various means. There are plans to extend the capacities and create more extention points on this, but those are still in development. Reports are entirely still in development and are not even in this branch. But once completed will allow for displaying form results that were saved to the database as documents.

## Custom Responders

If you need your forms to process results in a manner other than saving them to the database or emailing them you can create your own responder. Simply implement the CMSResponder interface. You will be given an opportunity to ask for configuration information, such as recieveing address, provide the name of your responder, and then lastly respond to the form. The form comes in as a json string, I recommend reverse engineering the email responder for a better grasp on this feature.

# REST APIs

There are currently two REST APIs which support the functions of this portlet. The Document API and the Form API. The Document API manages all document related tasks such as get, list, save, and delete as well as listing the document sources. The Form API adds a pair of other methods needed for forms and the form editor to work correctly, specifically responding to form submissions and listing available form responders. 
