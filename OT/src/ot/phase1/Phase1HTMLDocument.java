// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.phase1;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import gemini.sp.SpItem;
import gemini.util.HTMLAnchor;

public interface Phase1HTMLDocument
{
   public void generate(SpItem prog) throws IOException;
   public void generate(String filename, SpItem prog) throws IOException;
   public void generate(File file, SpItem prog) throws IOException;

   public HTMLAnchor[] getAnchors();
   public URL          getURL();
}

