// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

//
// This interface simply defines a few constants for item attributes used by
// the OT.
//
interface OtGuiAttributes
{
   //
   // This attribute is true if the item is collapsed, or not showing its
   // children.
   //
   String GUI_COLLAPSED = ".gui.collapsed";

   //
   // If this attribute is true, the item was selected when it was last
   // stored.
   //
   String GUI_SELECTED  = ".gui.selected";
}

