# Visual configurations { #capture_app_visual }


## Meta data sync error { #capture_app_visual_sync_error }


In case of errors during the sync process, a message is displayed in the settings menu ('Sync data' or 'Sync configuration' section). Also, a red sync icon is displayed next to the program in the Home screen. The sync error log gives details about the error and is prepared to be shared with admins.

![](resources/images/capture-app-image43.jpg){ width=25%}
![](resources/images/capture-app-image11.png){ width=25%}

You can also open the sync errors log from **Settings**:

![](resources/images/capture-app-image15.jpg){ width=25%}


## Personalize your Icon { #capture_app_visual_icon }


You can set your icon in the wallpaper using the DHIS 2 App Widget. The icon will be the flag configured in your server.

![](resources/images/capture-app-image32.jpg){ width=25%}

> **Note**
>
> How to add a widget:
>
> - Long press in your wallpaper
> - Select Widgets
> - Look for the DHIS 2 Widget
> - Select
>
> It will add a shortcut to your app.

## Personalise the colour of the App { #capture_app_visual_colour }


You can set the generic theme of the app by configuring the them in our server:

![](resources/images/capture-app-image28.png){width=80% }

> **Note**
>
> How to set the server theme and flag:
>
> Go to **System Settings > Appearance > Style**
>
> - Select your style
>   - Green,
>   - India (Orange),
>   - Light Blue,
>   - Myanmar (red),
>   - Vietnam
> - Select your flag
>
>![](resources/images/capture-app-image23.png){ width=25%}

## Icon library for metadata { #capture_app_visual_icon_lib }


There are currently 134 icons available that you can choose from. You can search by name in the icon picker widget. Icons are available in positive, negative and outlined shapes. We will be adding many more icons in future releases.

Icons can be associated to programs, stages, data elements and the options of an option set. This is  configurable through the Maintenance App.

> **Note**
>
> This feature is available from DHIS 2 2.30 onwards

![](resources/images/capture-app-image13.png){ .center width=80% }

In the Android App icons render in the home screen to illustrate all the programs available to a user, or while adding an stage. For data elements and options, the icons render for single event programs, when a section contains a single DE, showing the option set as a matrix or sequence of images.

![](resources/images/capture-app-image19.png){ width=25%}
![](resources/images/capture-app-image26.png){ width=25%}

> **Note**
>
> Icons will initially only render in the new Android app. The new web-based Capture App will incorporate them in the near future.

We plan to expand the collection of icons on each future version of DHIS2- for that we need your help: we are seeking ideas for new icons - please submit yours using [this form](https://www.google.com/url?q=https://drive.google.com/open?id%3D1LmfYJQAu3KyDfkY3X6ne7qSsuTa9jXZhoQHzkDxeCdg&sa=D&ust=1557433016147000).

## Colour palette for metadata { #capture_app_visual_colour_palette }


Tracker and Event capture now have the ability to render colours for programs, stages, data elements and options in option sets. A colour picker is integrated in the Maintenance App, which shows as a palette, except for options which allows the selection of any colour.

![](resources/images/capture-app-image20.png){ .center width=80% }

In the Android App, the color will be rendered as background color for programs and stages combined with an icon (if selected). For options it renders as the background colour during data entry form for single event programs. When the user selects a program with an assigned colour, that colour becomes the background theme for all screens in the domain of the program.

![](resources/images/capture-app-image19.png){ width=25%}
![](resources/images/capture-app-image2.jpg){ width=25%}

> **Note**
>
> Colours will first be available in the new Android app, followed in future releases by the new web-based Capture App.

## Rendering Types for Program Sections { #capture_app_visual_rendering_type }


![](resources/images/capture-app-image16.png){width=80% }

**Program Section**: when used in combination with icons, a Program Section with a single data element and associated Option Set can render the options in sequential or matrix layout (see screenshots). If the icon is not found, the app displays the DHIS logo.

![](resources/images/capture-app-image26.png){ width=25%}
![](resources/images/capture-app-image36.png){ width=25%}


> **Note**
>
> Render type for sections will first be available in the Android app, followed in future releases by the new web-based Capture App UI.

## Calendar { #capture_app_visual_calendar }


Now it is possible to use two different calendars. The one on the left is the first to appear but you can change it by clicking on the calendar icon on the lower left corner.

This is available when:

1. Making a new enrollment.
2. Creating a new event (Programs with and without registration).
3. Using period filters in data sets and programs with and without registration.

![](resources/images/capture-app-image60.png){ width=25%}
![](resources/images/capture-app-image61.png){ width=25%}

> **Note**
> 
> For Android versions 4.4,  5.1 and small devices, the Accept option is not visible in the second calendar view.

## Render types { #capture_app_visual_render }


The available rendering options have been expanded to include horizontal and vertical radio buttons, checkboxes and toggles. The allowed options depend on the value type.

- Yes Only: can be rendered as radio button or checkbox.

![](resources/images/capture-app-image111.jpg){ width=25%}

- Yes/No: can be rendered as horizontal/vertical radio buttons or horizontal/vertical checkboxes or toggle.

![](resources/images/capture-app-image112.jpg){ width=25%}

- Text: When is linked to an option set can be rendered as horizontal/vertical radio buttons or horizontal/vertical checkboxes. The option set must be configured with value type "Text", any other value type will be displayed as a drop-down list.

![](resources/images/capture-app-image110.jpg){ width=25%}

> **Note** 
>
> The default rendering option will automatically display a search box whenever there are more than 15 elements in the option set to ease the selection.
> 
> ![](resources/images/capture-app-options_no_search.png){ width=25% } ![](resources/images/capture-app-options_search.png){ width=60% }


### QR and Barcodes { #capture_app_visual_render_qr }


Data elements or attributes type text can be also configured as QR or barcodes. When a Data Element or Attribute is rendered as QR/Barcode, the app will open the device camera to read the code image. When the QR/Barcode is a TEI attribute configured as searchable, the user will be allowed to scan the code in order to search and identify the Tracked Entity Instance. This will also work for option sets.

Barcodes also allow the user to manually enter the value.

![](resources/images/capture-app-image118.png){ width=20%}
![](resources/images/capture-app-image119.png){ width=20%}
![](resources/images/capture-app-image120.png){ width=20%}
![](resources/images/capture-app-image121.png){ width=20%}
