package orac.ukirt.validation;

import java.util.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import gemini.sp.*;
import orac.ukirt.util.*;
import orac.ukirt.inst.*;
import orac.ukirt.iter.*;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.sp.iter.SpIterConfigBase;
import gemini.sp.iter.SpIterConfigObs;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterFolder;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpTelescopePosList;
import gemini.sp.SpObsContextItem;
import gemini.util.TelescopePos;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.iter.SpIterOffset;
import gemini.sp.iter.SpIterObserve;
import gemini.sp.iter.SpIterSky;
import gemini.sp.iter.SpIterChop;
import gemini.sp.obsComp.SpSiteQualityObsComp;
import orac.validation.*;

/**
 * Validation Tool.
 * 
 * This class is used for checking whether the values and settings in a Science Program or
 * Observation are sensible.
 *
 * Errors and warnings are issued otherwise.
 *
 * The class contains a main method and can be used as a stand alone command line tool to validate science programs.
 *
 * @author M.Folger@roe.ac.uk UKATC
 */
public class UkirtSpValidation extends SpValidation {

  static String [] instruments = {
                                   "CGS4",
				   "IRCAM3",
				   "UFTI",
				   "Michelle",
		                   "UIST"
                                 };

  /**
   * Set to true if type = SpType.ITERATOR_COMPONENT_TYPE = "ic" and subtype does NOT start with "inst...".
   */
  private boolean observeIteratorFound = false;

  /**
   * Set to true if type = ITERATOR_COMPONENT_TYPE = "ic" and subtype = "inst...".
   */
  private boolean instrumentIteratorFound = false;


  private int obsContainingDarkCount = 0;

  /**
   * Indicates whether the current is validation is a science program check or an observation check.
   * 
   * If this is set to true then the method checkObservation "knows" that it only has to check whether
   * there is a target list. If the target list is inside the observation its values will be checked
   * in checkContextItem (since an observation is also a context item). If the observation inherits its
   * target list from the science program then the target list values will be checked in checkObservation
   * only if the current check is an observation check (i.e. _isSpProgCheck is false). If the current check
   * is a science program check (i.e. _isSpProgCheck is true) then the target list check is done once in
   * checkSciProgram and not repaeted in all the observation checks.
   *
   * The same applies to checkDRRecipe.
   */
  private boolean _isSpProgCheck = false;

  /**
   * Resets fields to their original values.
   *
   * NEVER USED.
   *
   * This reset method is not as "important" as, say, CGS4Validation#reset because none of the values
   * that are reset is static and the values are all proberly initialised for each new UkirtSpValidation object.
   */
  public void reset() {
    observeIteratorFound    = false;
    instrumentIteratorFound = false;
    obsContainingDarkCount   = 0;
  }
  
  /**
   *
   */
  public UkirtSpValidation() {
    reset();
    CGS4Validation.reset();
  }

  /**
   * Searches for occurrence of a specified subclass of SpItem.
   * Prints every match highlighted.
   */
  public void findSpItemByClassName(SpItem spItem, String type, String tree_path) {
    SpItem child = null;
    Enumeration children = spItem.children();
    String result = null;
    
    if(spItem.getClass().toString().substring(6).equals(type)) {
      result = tree_path + "  *" + spItem.type().getType() + "*  ";
    }
    else {
      result = tree_path + "   " + spItem.type().getType() + "   ";
    }
      
    
    if(children.hasMoreElements()) {
      while (children.hasMoreElements()) {
        child = (SpItem) children.nextElement();
        
	findSpItemByClassName(child, type, result);
      }
    }
    else {
      System.out.println(result);
    }
  }

  /**
   * Returns true if SpItem of specified type and subtype is found in the subtree below SpItem argument.
   *
   * Uses the type and subtype strings defined in gemini.sp.SpType.
   * Returns immediately once a match is found without checking whether there might be more matches.
   */
  public boolean findSpItem(SpItem spItem, String type, String subtype) {
    
    boolean debug = false;
    
    if((System.getProperty("DEBUG") != null) && System.getProperty("DEBUG").equalsIgnoreCase("ON")) {
      debug = true;
      System.out.println("examining " + spItem.name() + ": " + spItem.typeStr() + " (" + spItem.subtypeStr() + "), "
                                                                                   + spItem.getClass().getName());
    }  


    if(spItem.typeStr().equals(type) && spItem.subtypeStr().equals(subtype)) {
      if(debug) {
        System.out.println("Returning true");
      }
      
      return true;
    }

    SpItem child = null;
    Enumeration children = spItem.children();
    boolean result = false;
    
    if(children.hasMoreElements() && (result == false)) {
      while (children.hasMoreElements()) {
        child = (SpItem) children.nextElement();
        
	result = findSpItem(child, type, subtype);
	if(result) {
          break;
	}
      }
    }
    
    if(debug) {
      System.out.println("Returning " + result);
    }

    return result;
  }

  /**
   * This method should not be called recursively.
   * It should be called from with in another method that checks all SpItem recursively.
   */
  public void checkInstrumentComponents(SpInstObsComp spInstObsComp, Vector report) {
    if(report == null) {
      report = new Vector();
    }

    if(spInstObsComp instanceof orac.ukirt.inst.SpInstMichelle) {
      (new MichelleValidation()).checkInstrument(spInstObsComp, report);
    }
    else if(spInstObsComp instanceof orac.ukirt.inst.SpInstUIST) {
      (new UISTValidation()).checkInstrument(spInstObsComp, report);
    }
    else if(spInstObsComp instanceof orac.ukirt.inst.SpInstIRCAM3) {
      (new IRCAM3Validation()).checkInstrument(spInstObsComp, report);
    }
    else if(spInstObsComp instanceof orac.ukirt.inst.SpInstUFTI) {
      (new UFTIValidation()).checkInstrument(spInstObsComp, report);
    }
    else if(spInstObsComp instanceof orac.ukirt.inst.SpInstCGS4) {
      (new CGS4Validation()).checkInstrument(spInstObsComp, report);
    }
    else {
      System.out.println("Unexpected instrument component: " + spInstObsComp.subtypeStr());
    }
  }  

  /**
   * Checks validity of observation.
   *
   * Like checkSciProgram this method might will be called recursively. So
   * its first argument is not always an SpObs. It can be any of the notes underneath an SpObs
   * in the program tree.
   * 
   * @param telescopeObsComp    Observation to be checked.
   * @param report              error messages and warnings are appended to report.
   */
  public void old_checkObservation(SpItem spItem, Vector report) {
       
    
    if(spItem instanceof SpIterObserveBase)
      observeIteratorFound = true;
    
    

    SpItem child = null;
    Enumeration children = spItem.children();
    
    if(children.hasMoreElements()) {
      while (children.hasMoreElements()) {
        child = (SpItem) children.nextElement();
        
	old_checkObservation(child, report);
      }
    }
  }

  /**
   *
   */
  public void checkInstrumentIterators(Vector iterators, Vector report) {

    if(iterators == null)
      return;
    
    // Checking for valid instrument component / instrument iterator combination.
    // First check whether the iterator is instrument specific.
    // This assumes that subtypes of instrument iterators start with "inst" (and NOT with "inst.")
    SpIterConfigObs currentIterator    = null;
    SpInstObsComp   currentInstObsComp = null;
    String          currentInstrument  = null;
    // iteratorInstrument is not an instrument iterator but the name of the instrument of an instrument iterator.
    String          iteratorInstrument = null;

    for(Enumeration e = iterators.elements(); e.hasMoreElements(); ) {
      currentIterator    = (SpIterConfigObs)e.nextElement();
      currentInstObsComp = SpTreeMan.findInstrument(currentIterator);
      
      if(currentInstObsComp != null) {
        currentInstrument  = currentInstObsComp.subtypeStr().substring(5);
	iteratorInstrument = currentIterator.subtypeStr().substring(4);

        for(int j = 0; j < instruments.length; j++) {
	  if(instruments[j].compareToIgnoreCase(iteratorInstrument) == 0) {

            // If the iterator is instrument specific then it should be for the current instrument.
            if((currentInstrument != null) && (currentInstrument.compareToIgnoreCase(iteratorInstrument) != 0)) {
              report.add(new ErrorMessage(ErrorMessage.ERROR,
	                                  iteratorInstrument + " iterator",
				          "You cannot use a " + iteratorInstrument +
	                                  " iterator in combination with " + currentInstrument + "."));
            }
	  }
        }	

        // Checking for invalid instrument component / FP iterator combinations.
        if(currentIterator instanceof SpIterFP) {
          if( currentInstObsComp instanceof SpInstCGS4 ||
              currentInstObsComp instanceof SpInstMichelle ||
              currentInstObsComp instanceof SpInstUIST ||
	      currentInstObsComp instanceof SpInstIRCAM3 ) {
	    report.add(new ErrorMessage(ErrorMessage.ERROR,
	                                "FP iterator",
		                        "You cannot use an FP iterator in combination with the instrument " +
	                                currentInstrument + "."));
          }
        }

        // Checking for invalid instrument component / IRPOL iterator combinations.
        if(currentIterator instanceof SpIterIRPOL) {
          if( currentInstObsComp instanceof SpInstMichelle ||
              currentInstObsComp instanceof SpInstUIST ||
	      currentInstObsComp instanceof SpInstIRCAM3 ) {
	    report.add(new ErrorMessage(ErrorMessage.ERROR,
	                                "IRPOL iterator",
	                                "You cannot use an IRPOL iterator in combination with the instrument " +
	                                currentInstrument + "."));
	  }
        }
      }
    }
  }

  /**
   * New checkObservation.
   *
   * This is not called recursively anymore.
   */
  public void checkObservation(SpObs spObs, Vector report) {

    observeIteratorFound     = false;
    instrumentIteratorFound  = false;

    report.add("------------------------------------------------------\n");

    // Check whether the observation has an instrument (as its own child OR in its context).
    if(SpTreeMan.findInstrument(spObs) != null) {
      if(!_isSpProgCheck) {
	checkInstrumentComponents(SpTreeMan.findInstrument(spObs), report);
      }	
    }

    // Check whether the observation has a target list (as its own child OR in its context).
    if(SpTreeMan.findTargetList(spObs) == null) {
      report.add(new ErrorMessage(ErrorMessage.WARNING,
                                  spObs.getTitle(),
				  "No target list."));
    }
    else {
      if(!_isSpProgCheck) {
	checkTargetList(SpTreeMan.findTargetList(spObs), report);
      }	
    }

    // Check whether the observation a DR recipe (as its own child OR in its context).
    if(SpTranslator.findRecipe(spObs) == null) {
      report.add(new ErrorMessage(ErrorMessage.WARNING,
	         spObs.getTitle(),
	         "No Dr-recipe component."));
    }
    else {
      if(!_isSpProgCheck) {
        checkDRRecipe(SpTranslator.findRecipe(spObs), report);
      }	
    }

    // Find iterators
    Vector observeIterators;
    Vector instrumentIterators;
    //Enumeration enumeration;
    SpItem tmpItem;

    // First find observe iterators.
    observeIterators = findInstances(spObs, "gemini.sp.iter.SpIterObserveBase");
    
    if(observeIterators == null) {
      report.add(new ErrorMessage(ErrorMessage.ERROR,
	         spObs.getTitle(),
		 "No observe iterator."));
    }
    
    // check for DARK observe iterators.
    observeIterators = findInstances(spObs, "orac.ukirt.iter.SpIterDarkObs");
    
    
    if(observeIterators != null && observeIterators.size() > 0) {
      obsContainingDarkCount++;
    }
    
    // Now find instrument iterators.
    instrumentIterators = findInstances(spObs, "gemini.sp.iter.SpIterConfigObs");

    if(instrumentIterators == null) {
      if(SpTreeMan.findInstrument(spObs) == null) {
        report.add(new ErrorMessage(ErrorMessage.ERROR,
	                            spObs.getTitle(),
				    "Neither instrument iterator nor instrument component found."));
      }				    
    }
    else {
      checkInstrumentIterators(instrumentIterators, report);
    }

    // observeIterators    still contains DARK observe iterators and
    // instrumentIterators stiil contains all instrument iterators
    SpInstObsComp instObsComp = null;
    if(observeIterators != null) {
      double instExpTime = 0;
      double darkExpTime = 0;
      instObsComp = SpTreeMan.findInstrument(spObs);
      SpIterDarkObs darkObs;
      SpIterConfigObs tmpInstIter = null;
      double instIterExpTime = 0;
    
      if(instObsComp != null)
        instExpTime = instObsComp.getExposureTime();

      for(Enumeration e1 = observeIterators.elements(); e1.hasMoreElements(); ) {
        darkExpTime = ((SpIterObserveBase)e1.nextElement()).getExposureTime();

        if(darkExpTime != instExpTime) {
            report.add(new ErrorMessage(ErrorMessage.WARNING,
	                                spObs.getTitle(),
				        "DARK doesn't have same exposure time as instrument component."));
        }	
        
	if(instrumentIterators != null) {
          for(Enumeration e2 = instrumentIterators.elements(); e2.hasMoreElements(); ) {
	    tmpInstIter = (SpIterConfigObs)e2.nextElement();
	    if(tmpInstIter != null && tmpInstIter.getExposureTimes() != null) {
	      for(Enumeration e3 = tmpInstIter.getExposureTimes().elements(); e3.hasMoreElements(); ) {
	        try {
	          instIterExpTime = Double.parseDouble((String)e3.nextElement());
	        }
	        catch(NumberFormatException e) {
                  e.printStackTrace();
	        }

	        if(darkExpTime != instIterExpTime) {
                  report.add(new ErrorMessage(ErrorMessage.WARNING,
	                                      spObs.getTitle(),
				              "DARK doesn't have same exposure time as instrument iterator."));
	        }
	      }
	    }
          }
	}
      }
    }
    
    // Checking that there is no offset followed by an instrument in the sequence and whether an SpIterObserve in the
    // sequence is followed by an instrument iterator or an offset iterator.
    SpItem previousItem = null;
    SpItem currentItem  = null;
    SpIterFolder iterFolder = null;
    boolean spIterObserveFound = false;
    boolean dark = false;
    Vector iterFolderVector = findInstances(spObs, "gemini.sp.iter.SpIterFolder");
    if(iterFolderVector != null) {
      for(Enumeration e1 = iterFolderVector.elements(); e1.hasMoreElements(); ) {
        iterFolder = (SpIterFolder)e1.nextElement();
        for(Enumeration e2 = iterFolder.children(); e2.hasMoreElements(); ) {
          previousItem = currentItem;
          currentItem  = (SpItem)e2.nextElement();
	
	  if((previousItem instanceof SpIterOffset) && (currentItem instanceof gemini.sp.iter.SpIterConfigObs)) {
            report.add(new ErrorMessage(ErrorMessage.WARNING,
	                                "Iterator Folder in " + spObs.getTitle(),
			                "There is an offset iterator followed by an instrument iterator."));
	  }

	  if(currentItem instanceof SpIterObserve) {
            spIterObserveFound = true;
	  }

	  if(spIterObserveFound) {
	    if(currentItem instanceof gemini.sp.iter.SpIterConfigObs) {
              report.add(new ErrorMessage(ErrorMessage.ERROR,
	                                  "Iterator Folder in " + spObs.getTitle(),
		 		          "Instrument iterators are not allowed after the observe."));
	    }
	  }
          
	  if(currentItem instanceof SpIterDarkObs) {
            dark = true;
	  }
        }
        
        instObsComp = SpTreeMan.findInstrument(spObs);
        if((instObsComp instanceof SpInstUFTI) || (instObsComp instanceof SpInstIRCAM3)) {
          if(!dark) {
            report.add(new ErrorMessage(ErrorMessage.WARNING,
	                                spObs.getTitle(),
		 		        "UFTI and IRCAM3 require DARK in sequence."));
	  }
        }
      }
    }
  
    // check for position iterators or instrument iterators that are on the same level of iteration as
    // the OBSERVE observe iterator.
    observeIterators = findInstances(spObs, "gemini.sp.iter.SpIterObserve");
    SpIterObserve iterObserve = null;
    SpItem        sibling     = null;
    boolean wrongIteratorsOnObserveLevel = false;

    if(observeIterators != null) {
      for(Enumeration e = observeIterators.elements(); e.hasMoreElements(); ) {
        iterObserve = (SpIterObserve)e.nextElement();
	
	sibling = iterObserve.next();
	while((sibling != null) && (!wrongIteratorsOnObserveLevel)) {
          if((sibling instanceof SpIterConfigObs) || (sibling instanceof SpIterOffset)) {
            wrongIteratorsOnObserveLevel = true;
	  }
	  sibling = sibling.next();
	}

	sibling = iterObserve.prev();
	while((sibling != null) && (!wrongIteratorsOnObserveLevel)) {
          if((sibling instanceof SpIterConfigObs) || (sibling instanceof SpIterOffset)) {
            wrongIteratorsOnObserveLevel = true;
	  }
	  sibling = sibling.prev();
	}

        if(wrongIteratorsOnObserveLevel) {
          report.add(new ErrorMessage(ErrorMessage.WARNING,
	                              spObs.getTitle(),
		 		      "The position iterator or instrument iterator and the \"observe\" are on the same level of iteration."));
	}
	
      }
    } 
  }

  /**
   * Checks validity of target list.
   * 
   * @param telescopeObsComp    Target list to be checked.
   * @param report              error messages and warnings are appended to report.
   */
  public void checkTargetList(SpTelescopeObsComp telescopeObsComp, Vector report) {
    SpTelescopePosList list = telescopeObsComp.getPosList();
    TelescopePos [] position = list.getAllPositions();
    double [] values = null;
    double deg;
    double min;
    double sec;
	
    boolean hasGuideStar = false;
    
    SpTelescopePos pos     = null;
    SpTelescopePos pos2    = null;
    String trackingSystem  = null;
    String trackingSystem2 = null;
    for(int i = 0; i < position.length; i++) {
      pos = (SpTelescopePos)position[i];
      hasGuideStar = pos.isGuidePosition();

      try {
        trackingSystem  = (String)telescopeObsComp.getTable().getAll(pos.getTag() ).get(4);
      }
      catch(Exception e) {
        // catching possible NullPointerException or ArrayIndexOutOfBoundsException
        e.printStackTrace();
      }

        // Checking RA aka x axis
        // Note that getXaxis() maps values in the range 0..24 h typed by the user to degrees 0..360.
        // It also works modulo 24. So 12:00:00.0 and 36:00:00.0 both yield 180.0.
	// getXaxis also treats things like 9h 16min as equivalent to 8h 76min etc.
	
        // But the user should not be allowed to type 36h instead of 12h etc so checking is based on the
	// original string.
        
	try {
          values = degreeString2doubles(pos.getXaxisAsString());
	  deg = values[0];
	  min = values[1];
	  sec = values[2];
	
	
          // Checking allowed ranges. Overall range allowded: 0 - 24 hours
          if(deg < 0 || deg >= 24
          || min < 0 || min >= 60
          || sec < 0 || sec >= 60) {
        
  	    report.add(new ErrorMessage(ErrorMessage.ERROR,
	                                "Telescope target " + pos.getName(),
					"RA",
				        "range 0:00:00 .. 24:00:00",
				        pos.getXaxisAsString()));
          }
	}
	catch(NumberFormatException e) {
	  report.add(new ErrorMessage(ErrorMessage.ERROR,
                                      "Telescope target " + pos.getName(),
				      "RA",
				      "Format <hours>:<min>:<sec>",
				      pos.getXaxisAsString()));
	}

	
	try {
          values = degreeString2doubles(pos.getYaxisAsString());
	  deg = values[0];
	  min = values[1];
	  sec = values[2];

          // Checking allowed ranges. Overall range allowed: -40..60
          if(deg < -40 || deg >  60
          || min < 0   || min >= 60
          || sec < 0   || sec >= 60
          || (deg >= 60  && min > 0)
	  || (deg >= 60  && sec > 0)
	  || (deg <= -40 && min > 0)
	  || (deg <= -40 && sec > 0)) {
        
	    report.add(new ErrorMessage(ErrorMessage.ERROR,
	                                "Telescope target " + pos.getName(),
					"Dec",
				        "range -40:00:00 .. 60:00:00",
				        pos.getYaxisAsString()));
          }
	}
	catch(NumberFormatException e) {
	  report.add(new ErrorMessage(ErrorMessage.ERROR,
                                      "Telescope target " + pos.getName(),
				      "Dec",
				      "Format <deg>:<min>:<sec>",
				      pos.getYaxisAsString()));
	}

	// checking whether both RA and Dec are 0:00:00
	if(pos.getXaxis() == 0 && pos.getYaxis() == 0) {
	  report.add(new ErrorMessage(ErrorMessage.WARNING,
                                      "Telescope target " + pos.getName(),
				      "Both Dec and RA are 0:00:00"));
	}

        
	// Check whether the target list has a target coordinate for which the epoch is B1950, 
        // and a guide star coordinate for which the Ra and Dec values are identical 
        // and the epoch is J2000.
	for(int j = 0; j < position.length; j++) {
          pos2 = (SpTelescopePos)position[j];

          // Do not compare a position with itself
	  if(i != j) {

	    if((pos.getXaxis() == pos2.getXaxis()) &&
	       (pos.getYaxis() == pos2.getYaxis())) {

	      try {
	        trackingSystem2 = (String)telescopeObsComp.getTable().getAll(pos2.getTag()).get(4);
	      }
	      catch(Exception e) {
                // catching possible NullPointerException or ArrayIndexOutOfBoundsException
	        e.printStackTrace();
              }
	    
	      if(trackingSystem != null  && trackingSystem2 != null &&
	           pos.isGuidePosition() && trackingSystem.equals( "FK5 (J2000)") &&
	         !pos2.isGuidePosition() && trackingSystem2.equals("FK4 (B1950)")) {
	      
	        report.add(new ErrorMessage(ErrorMessage.ERROR,
                                            telescopeObsComp.getTitle(),
				            "Target coordinate with B1950 and guide star coordinate with J2000 have identical values for Ra and Dec."));
	      }
	    }
	  }  
	}  
    }
  
    if(!hasGuideStar) {
      report.add(new ErrorMessage(ErrorMessage.WARNING,
                                  telescopeObsComp.getTitle(),
				  "No guide star specified."));
    }
  }

  public void checkDRRecipe(SpDRRecipe recipe, Vector report) {
        
    if(!(
      recipe.getArcInGroup() ||
      recipe.getBiasInGroup() ||
      recipe.getDarkInGroup() ||
      recipe.getFlatInGroup() ||
      recipe.getObjectInGroup() ||
      recipe.getSkyInGroup()
    )) {
      report.add(new ErrorMessage(ErrorMessage.WARNING,
                                  "DR Recipe in " + recipe.parent().getTitle(),
				  "No part included in group."));
    }

    SpInstObsComp inst = ((SpInstObsComp) SpTreeMan.findInstrument(recipe));
    
    if(inst == null) {
      report.add(new ErrorMessage(ErrorMessage.WARNING,
                                  "DR Recipe in " + recipe.parent().getTitle(),
				  "Can't find instrument in scope."));
    }
    else {
      Vector recipes = null;
      
      // NOTE that _IN_GROUP_ recipes are not considered because they are currently all set to false
      // in SpDRRecipe.
      if(inst instanceof SpInstUFTI) {
        recipes = SpDRRecipe.UFTI.getColumn(0);
	recipes.add(SpDRRecipe.UFTI_DARK_RECIPE_DEFAULT);
	recipes.add(SpDRRecipe.UFTI_SKY_RECIPE_DEFAULT);
	recipes.add(SpDRRecipe.UFTI_OBJECT_RECIPE_DEFAULT);

        if(!recipes.contains(recipe.getDarkRecipeName()))
          report.add(new ErrorMessage(ErrorMessage.WARNING,
                                      "DR Recipe (Dark) for UFTI",
				      recipe.getDarkRecipeName() + " not in the OT list."));
            
        if(!recipes.contains(recipe.getSkyRecipeName()))
          report.add(new ErrorMessage(ErrorMessage.WARNING,
                                      "DR Recipe (Sky) for UFTI",
				      recipe.getSkyRecipeName() + " not in the OT list."));
        
	if(!recipes.contains(recipe.getObjectRecipeName()))
          report.add(new ErrorMessage(ErrorMessage.WARNING,
                                      "DR Recipe (Object) for UFTI",
				      recipe.getObjectRecipeName() + " not in the OT list."));
      }
      else if(inst instanceof SpInstIRCAM3) {
        recipes = SpDRRecipe.IRCAM3.getColumn(0);
	recipes.add(SpDRRecipe.IRCAM3_BIAS_RECIPE_DEFAULT);
	recipes.add(SpDRRecipe.IRCAM3_DARK_RECIPE_DEFAULT);
	recipes.add(SpDRRecipe.IRCAM3_SKY_RECIPE_DEFAULT);
	recipes.add(SpDRRecipe.IRCAM3_OBJECT_RECIPE_DEFAULT);

        if(!recipes.contains(recipe.getBiasRecipeName()))
          report.add(new ErrorMessage(ErrorMessage.WARNING,
                                      "DR Recipe (Bias) for IRCAM3",
				      recipe.getBiasRecipeName() + " not in the OT list."));
     
        if(!recipes.contains(recipe.getDarkRecipeName()))
          report.add(new ErrorMessage(ErrorMessage.WARNING,
                                      "DR Recipe (Dark) for IRCAM3",
				      recipe.getDarkRecipeName() + " not in the OT list."));
      
        if(!recipes.contains(recipe.getSkyRecipeName()))
          report.add(new ErrorMessage(ErrorMessage.WARNING,
                                      "DR Recipe (Sky) for IRCAM3",
				      recipe.getSkyRecipeName() + " not in the OT list."));
        
	if(!recipes.contains(recipe.getObjectRecipeName()))
          report.add(new ErrorMessage(ErrorMessage.WARNING,
                                      "DR Recipe (Object) for IRCAM3",
				      recipe.getObjectRecipeName() + " not in the OT list."));
      }
      else if(inst instanceof SpInstCGS4) {
        recipes = SpDRRecipe.CGS4.getColumn(0);
	recipes.add(SpDRRecipe.CGS4_BIAS_RECIPE_DEFAULT);
	recipes.add(SpDRRecipe.CGS4_DARK_RECIPE_DEFAULT);
	recipes.add(SpDRRecipe.CGS4_FLAT_RECIPE_DEFAULT);
	recipes.add(SpDRRecipe.CGS4_ARC_RECIPE_DEFAULT);
	recipes.add(SpDRRecipe.CGS4_SKY_RECIPE_DEFAULT);
	recipes.add(SpDRRecipe.CGS4_OBJECT_RECIPE_DEFAULT);

        if(!recipes.contains(recipe.getBiasRecipeName()))
          report.add(new ErrorMessage(ErrorMessage.WARNING,
                                      "DR Recipe (Bias) for CGS4",
				      recipe.getBiasRecipeName() + " not in the OT list."));

        if(!recipes.contains(recipe.getDarkRecipeName()))
          report.add(new ErrorMessage(ErrorMessage.WARNING,
                                      "DR Recipe (Dark) for CGS4",
				      recipe.getDarkRecipeName() + " not in the OT list."));
        
	if(!recipes.contains(recipe.getFlatRecipeName()))
          report.add(new ErrorMessage(ErrorMessage.WARNING,
                                      "DR Recipe (Flat) for CGS4",
				      recipe.getFlatRecipeName() + " not in the OT list."));
     
	if(!recipes.contains(recipe.getArcRecipeName()))
          report.add(new ErrorMessage(ErrorMessage.WARNING,
                                      "DR Recipe (Arc) for CGS4",
				      recipe.getArcRecipeName() + " not in the OT list."));
              
        if(!recipes.contains(recipe.getSkyRecipeName()))
          report.add(new ErrorMessage(ErrorMessage.WARNING,
                                      "DR Recipe (Sky) for CGS4",
				      recipe.getSkyRecipeName() + " not in the OT list."));

	if(!recipes.contains(recipe.getObjectRecipeName()))
          report.add(new ErrorMessage(ErrorMessage.WARNING,
                                      "DR Recipe (Object) for CGS4",
				      recipe.getObjectRecipeName() + " not in the OT list."));
      }
      else {
        System.out.println("No dr recipes for instrument " + inst.subtypeStr());
      }
    }
  }

  /**
   * Convert from an RA in HH:MM:SS string format to degrees, minutes, seconds.
   *
   * @see gemini.util.HHMMSS#valueOf  
   */
  public double [] degreeString2doubles(String s) throws NumberFormatException {
    if (s == null) throw new NumberFormatException("null argument");
    
    if (s.length() < 1) throw new NumberFormatException("empty string");

    // Determine the sign from the (trimmed) string
    s = s.trim();
    int sign = 1;
    if(s.charAt(0) == '-') {
      sign = -1;
      s    = s.substring(1);
    }

    // Parse the string into values for hours, min, and sec
    // Initialize with 0.0 but these default values should only beused if there is NO other value specified.
    // If something invalid is specified an exception should be thrown.
    double[] vals = {0.0, 0.0, 0.0};
    StringTokenizer tok = new StringTokenizer(s, ": ");
    for(int i=0; i<3 && tok.hasMoreTokens(); i++) {
      vals[i] = Double.valueOf(tok.nextToken()).doubleValue();
    }

    vals[0] *= sign;

    return vals;
  }

  /**
   * Checks validity of the whole SciProgram tree.
   * checkSciProgram is not just a top level class applied to SpProg objects. It is applied recursively
   * to an spItem's children until a target list object or an observation object is found and checkTargetList or
   * checkObservation, respectively, called.
   * 
   * @param telescopeObsComp    Science program item to be checked.
   * @param report              error messages and warnings are appended to report.
   */
  public void checkSciProgram(SpProg spProg, Vector report) {
    _isSpProgCheck = true;

    if(SpTreeMan.findAllItems(spProg, "gemini.sp.SpObs").size() < 1) {
      report.add(new ErrorMessage(ErrorMessage.WARNING,
                                  spProg.getTitle(),
				  "No observation."));        
      return; 
    }

    // Check context items (e.g folders, groups, observations)
    // Note that observations are checked twice: as context items AND in checkObservation for
    // where things specific to observations are checked.
    //
    // Start by calling checkContextItem for the science program itself
    checkContextItem(spProg, report);
    
    // Checking for standard observation.
    boolean standardObsFound = false;
    Vector instances = findInstances(spProg, "gemini.sp.SpObs");
    if(instances != null) {
      for(Enumeration e = instances.elements(); e.hasMoreElements(); ) {
        if(((SpObs)e.nextElement()).getIsStandard()) {
          standardObsFound = true;
	  break;
        }
      }
    }

    if(!standardObsFound) {
      report.add(new ErrorMessage(ErrorMessage.WARNING,
	                          spProg.getTitle(),
	                          "No observation has been choosen as standard."));
    }  
    
    // CGS4 checks

    // Checking CGS4 masks
    if(CGS4Validation.getMasks().size() > 1) {
      report.add(new ErrorMessage(ErrorMessage.WARNING,
	                          spProg.getTitle() + ", CGS4",
	                          "More than one slit width is specified."));
    }
    
    // Checking CGS4 samplings
    if(CGS4Validation.getSamplings().size() > 1) {
      report.add(new ErrorMessage(ErrorMessage.WARNING,
	                          spProg.getTitle() + ", CGS4",
	                          "More than one sampling width is specified."));
    }
    
    // Checking CGS4 position angles
    if(CGS4Validation.getPosAngles().size() > 1) {
      report.add(new ErrorMessage(ErrorMessage.WARNING,
	                          spProg.getTitle() + ", CGS4",
	                          "More than one position angle is specified."));
    }

    // Checking CGS4 calibration
    instances = findInstances(spProg, "orac.ukirt.inst.SpInstCGS4");
    
    // if there are observations that use CGS4 check whether there is a CGS4 calibration observation
    // i.e. an observation containing both ARC and FLAT
    if(instances != null && instances.size() > 0) {
      if(findCGS4CalObservation(spProg) == null) {
        report.add(new ErrorMessage(ErrorMessage.WARNING,
	                          spProg.getTitle() + ", CGS4",
	                          "There are one or more observations that use CGS4 but there is no observation that contains a FLAT and an ARC."));
      }
    }

/*
    // Checking for array test. (Currently not used)
    instances = new Vector();
    findInstances(spProg, "gemini.sp.obsComp.SpInstObsComp", instances);
    
    if(instances.size() > 0) {
      if(findArrayTests(spProg) == null) {
        report.add(new ErrorMessage(ErrorMessage.WARNING,
	                            spProg.getTitle(),
	                            "No array test."));
      }
    }
*/

    _isSpProgCheck = false;

    super.checkSciProgram(spProg, report);
  }

  
  /**
   * This method is used to check the SpProg, SpObsGroup and SpObsFolder.
   *
   * In the case of SpProg check is called from within checkSciProgram which contains adtional checks.
   * The method is NOT called recursively!!!
   */
  public void checkContextItem(SpObsContextItem contextItem, Vector report) {

    // check whether spItem is an observation.
    if(contextItem instanceof SpObs) {
      checkObservation((SpObs)contextItem, report);
    }  

    Enumeration children = contextItem.children();
    while (children.hasMoreElements()) {
      SpItem spItem = (SpItem)children.nextElement();

    
      if(spItem instanceof SpIterFolder && !(contextItem instanceof SpObs)) {
        report.add(new ErrorMessage(ErrorMessage.WARNING,
                                    "Iterator Folder in " + contextItem.getTitle(),
				    "Iterator folders should only be used inside an observation."));      
      }
      else if(spItem instanceof SpInstObsComp) {
        // This assumes that the instrument component appears above the instrument iterator in the tree.
        // And it assumes that subtypes of instrument component start with "inst." (and NOT with "inst").
        //currentInstrument = spItem.subtypeStr().substring(5);
        checkInstrumentComponents((SpInstObsComp)spItem, report);
      }
      // Check Target List.
      else if(spItem instanceof SpTelescopeObsComp) {
        checkTargetList((SpTelescopeObsComp)spItem, report);
      }
      // Check DR recipe.
      else if(spItem instanceof SpDRRecipe) {
        checkDRRecipe((SpDRRecipe)spItem, report);
      }
      else if(spItem instanceof SpObsContextItem) {
        checkContextItem((SpObsContextItem)spItem, report);
      }
	
      // Check other things? 
    }
  }  


  /**
   * Find all SpItems below spItem that are instances of the class className.
   * Similar to gemini.sp.SpTreeMan.findAllItems but gemini.sp.SpTreeMan.findAllItems returns only
   * exact matches. This method returns spItems whose class is a subtype of that specified by classname too.
   *
   * @param result the instances are appended to the result vector. So findInstances can be called several
   *               times with the same vactor so that instances can be accumulated.
   *
   * @see gemini.sp.SpTreeMan#findAllItems
   */
  public static void findInstances(SpItem spItem, Class c, Vector result) {
    if(result == null)
      result = new Vector();
      
    if(c.isInstance(spItem))
      result.add(spItem);

    SpItem child = null;
    Enumeration children = spItem.children();
    
    if(children.hasMoreElements()) {
      while (children.hasMoreElements()) {
        child = (SpItem) children.nextElement();
        
	findInstances(child, c, result);
      }
    }
  }

  public static void findInstances(SpItem spItem, String className, Vector result) {
    try {
      findInstances(spItem, Class.forName(className), result);
    }
    catch(ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * @see #findInstances(gemini.sp.SpItem, java.lang.Class, java.util.Vector)
   */
  public static Vector findInstances(SpItem spItem, Class c) {
    Vector result = new Vector();
    findInstances(spItem, c, result);

    if(result.size() > 0)
      return result;

    else
      return null;
  }

  public static Vector findInstances(SpItem spItem, String className) {
    Vector result = null;
    
    try {
      result = findInstances(spItem, Class.forName(className)); 
    }
    catch(ClassNotFoundException e) {
      e.printStackTrace();
    }

    return result;
  }

  /**
   * This method looks for observations whose iterator folder contains at least 2 observe iterators DARK and no other
   * observe iterators.
   *
   * Such observations are assumed to be array tests.
   *
   * @return array test observations
   */
  public static Vector findArrayTests(SpProg spProg) {
    Vector result           = new Vector();
    Vector observations     = null;
    Vector observeIterators = null;
    Vector darkIterators    = null;
    SpObs spObs             = null;

    observations = findInstances(spProg, "gemini.sp.SpObs");

    if(observations != null) {
      // Go through observations.
      for(Enumeration e = observations.elements(); e.hasMoreElements(); ) {
        spObs = (SpObs)e.nextElement();
        observeIterators = findInstances(spObs, "gemini.sp.iter.SpIterObserveBase");
        darkIterators    = findInstances(spObs, "orac.ukirt.iter.SpIterDarkObs");

        // check that there are at least two dark iterators.
        if(darkIterators != null && darkIterators.size() >= 2) {
          // check whether that there are no observe iterators other than dark iterators.
          if(observeIterators != null && observeIterators.size() == darkIterators.size()) {
            result.add(spObs);
	  }
        }	
      }
    }

    if(result.size() > 0)
      return result;

    else
      return null;
  }


  /**
   * This method looks for observations whose iterator folder contains at least one CGS4 calibration observe iterator
   * ARC and one CGS4 calibration observe iterator FLAT.
   *
   * @return matching observations.
   */
  public static Vector findCGS4CalObservation(SpProg spProg) {
    Vector result              = new Vector();
    Vector cgs4CalObsIterators = null;
    Vector observations        = null;
    boolean arc                = false;
    boolean flat               = false;
    SpObs spObs                = null;

    observations = findInstances(spProg, "gemini.sp.SpObs");
    
    if(observations != null) {
      // Go through observations.
      for(Enumeration e = observations.elements(); e.hasMoreElements(); ) {
        spObs = (SpObs)e.nextElement();
        cgs4CalObsIterators = findInstances(spObs, "orac.ukirt.iter.SpIterCGS4CalObs");

        arc  = false;
        flat = false;
      
        if(cgs4CalObsIterators != null) {
	  for(Enumeration e2 = cgs4CalObsIterators.elements(); e2.hasMoreElements(); ) {
	    // ARC
	    if(((SpIterCGS4CalObs)e2.nextElement()).getCalType() == SpIterCGS4CalObs.ARC)
	      arc = true;

	    // FLAT
	    else
	      flat = true;
	  }
	  if(arc && flat) {
            result.add(spObs);
	  }
        }
      }
    }

    if(result.size() > 0)
      return result;

    else
      return null;
  }


/**
 * Print a formatted string representation of the item with the given
 * indentation for debugging.
 *
 * Based on method gemini.sp.SpItem.print.
 */
public void
print(SpItem spItem, String indentStr)
{
   String t = spItem.name() + ": " + spItem.typeStr() + " (" + spItem.subtypeStr() + "), "
                                                                                   + spItem.getClass().getName();
   System.out.println(indentStr + t);

   indentStr = indentStr + "   ";
  
   // Print the attributes
   Enumeration keys = spItem.getTable().attributes();
   while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      System.out.println(indentStr+key+" ("+ spItem.getTable().getDescription(key)+")");
      System.out.println(indentStr + "--> " + spItem.getTable().getAll(key).toString());
   }

   // Print the children
   for (SpItem child = spItem.child(); child != null; child = child.next()) {
      print(child, indentStr + "   ");
   }
}

  /**
   * UKIRT specific version of the method.
   *
   * This UKIRT specific version of the checkMSB method does currently not
   * check for observation components because this is still done
   * in checkObservation. However the UKIRT checkObservation method is
   * slightly out of date as it does not take into account MSBs.
   * At some point the checking for instrument components should be
   * unified according to the OMP system requirements which would make
   * this UKIRT specific checkMSB method obsolete.
   * The checkMSB method of the parent class could then be used.
   */
  public void checkMSB(SpMSB spMSB,  Vector report) {

    if(spMSB instanceof SpObs) {
      checkObservation((SpObs)spMSB, report);
    }
    else {

      Enumeration children = spMSB.children();
      SpItem child;

      while(children.hasMoreElements()) {
        child = (SpItem)children.nextElement();

        if(child instanceof SpObs) {
          checkObservation((SpObs)child, report);
        }  
      }
    }
  }


  public static void main(String [] args) {
    
    if(args.length < 1) {
      System.err.println("Usage: UkirtSpValidation <input SGML file>");
      System.exit(1);
    }

    UkirtSpValidation c = new UkirtSpValidation();
    
    // Create instances of the UKIRT items - this will cause them to be
    // registered with SpFactory - necessary so that the component subtypes
    // can be understood.
    SpItem spItem = new SpInstUFTI();
    spItem = new SpInstCGS4();
    spItem = new SpInstIRCAM3();
    spItem = new SpInstMichelle();
    spItem = new SpInstUIST();
    spItem = new SpDRRecipe();
    spItem = new SpIterBiasObs();
    spItem = new SpIterDarkObs();
    spItem = new SpIterCGS4();
    spItem = new SpIterMichelle();
    spItem = new SpIterUFTI();
    spItem = new SpIterIRCAM3();
    spItem = new SpIterCGS4CalUnit();
    spItem = new SpIterCGS4CalObs();
    spItem = new SpIterFP();
    spItem = new SpIterIRPOL();
    spItem = new SpIterNod();
    spItem = new SpIterChop();
    spItem = new SpIterObserve();
    spItem = new SpIterSky();
    spItem = new SpSiteQualityObsComp();

    SpRootItem root = null;
    SpInputSGML inSGML = null;
    try {
      
      inSGML = new SpInputSGML(new FileReader(args[0]));
      root = inSGML.parseDocument();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    
   

    int ch;
    boolean correct_input = false;
    
    try {
      LineNumberReader  lineReader  = new LineNumberReader(new InputStreamReader(System.in));
      String type;
      String subtype;
      
      while(correct_input == false) {
        System.out.println("Choose one option:");
        System.out.println("  [p]rint");
        //System.out.println("  [f]ind");
        //System.out.println("  find c[l]ass");
	System.out.println("  [c]heck science program");
   
	ch = lineReader.readLine().toCharArray()[0];

        switch(ch) {
          case 'p':
            c.print(root, "");
	    correct_input = true;
  	    break;
      
          case 'c':
	    Vector report = new Vector();
            c.checkSciProgram((SpProg)root, report);
            ErrorMessage.printMessages(report.elements(), System.out);
	    correct_input = true;
  	    break;
	  
	  //case 'f': 
	  //  System.out.println("Type?");
	  //  type = lineReader.readLine();
	  //  System.out.println("Sub type?");
	  //  subtype = lineReader.readLine();
          //  System.out.println("Search Result: " + c.findSpItem(root, type, subtype));
	  //  correct_input = true;
          //  break;
          //
          //case 'l': 
	  //  System.out.println("Type?");
	  //  type = lineReader.readLine();
          //  c.findSpItemByClassName(root, type, "");
	  //  correct_input = true;
          //  break;

	  default: System.out.println("You typed an invalid character.");
        }
      }
      lineReader.close();
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }  
}

