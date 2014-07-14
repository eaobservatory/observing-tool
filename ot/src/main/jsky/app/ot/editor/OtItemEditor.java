// Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1) Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// 2) Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// 3) The names of AURA and its representatives may not be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
// FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
// GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
// THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//
// $Id$
//
package jsky.app.ot.editor ;

import javax.swing.JPanel ;
import javax.swing.JTextArea ;
import jsky.app.ot.OtItemEditorWindow ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import gemini.sp.SpAvTable ;
import gemini.sp.SpItem ;
import gemini.util.Assert ;
import jsky.app.ot.tpe.TpeManager ;
import jsky.app.ot.tpe.TelescopePosEditor ;


/**
 * This is the base class for all SpItem editors.  It uses a
 * presentation, but does not contain it in the sense of being its
 * parent in the layout hierarchy.  So the OtItemEditorWindow,
 * which does contain the presentation, delegates events to this class.
 */
public abstract class OtItemEditor
{
	/** Title to display in bold font on the OtItemEditorWindow. */
	protected String _title ;

	/** Reference to the containing OtItemEditorWindow (needed to set the title) */
	protected OtItemEditorWindow _otItemEditorWindow ;

	/** Brief description of the editor to display on the OtItemEditorWindow. */
	protected String _description ;

	/** The panel containing the presentation gui. */
	protected JPanel _presSource ;

	/** Can this editor be resized? */
	protected boolean _resizable ;

	/** The SpItem being configured with this editor. */
	protected SpItem _spItem ;

	/** The attribute/value table of the current SpItem. */
	protected SpAvTable _avTab ;

	/** The loaded Presentation panel that contains the gui. */
	protected JPanel _pres ;

	// Widgets in the OtItemEditorWindow
	private JTextArea _descriptionWidget ;

	/** Default constructor */
	public OtItemEditor(){}

	/**
	 * Get the title.  The title should be set by the subclass.
	 */
	public String getTitle()
	{
		return _title ;
	}

	/**
	 * Get the description.  The description should be set by the subclass.
	 */
	public String getDescription()
	{
		return _description ;
	}

	/**
	 * Get the panel containing the presentation.  The gui
	 * should be set by the subclass ;
	 */
	public JPanel getPresSource()
	{
		return _presSource ;
	}

	/**
	 * Get the "resizable" property.  If the editor subclass is resizable,
	 * then it should reset _resizable to true.  Otherwise, the default (false)
	 * is returned.
	 */
	public boolean isResizable()
	{
		return _resizable ;
	}

	/**
	 * Set the "resizable" property.  
	 */
	public void setResizable( boolean b )
	{
		_resizable = b ;
	}

	/**
	 * Set the presentation containing the editor's widgets.  Subclasses may
	 * override _init to do any required setup.
	 */
	public void setPresentation( JPanel presentation )
	{
		_pres = presentation ;
		_init() ;
	}

	/**
	 * Set a reference to the containing OtItemEditorWindow (needed to set the
	 * title of the window).
	 */
	public void setOtItemEditorWindow( OtItemEditorWindow w )
	{
		_otItemEditorWindow = w ;
	}

	/**
	 * Set the widget that displays the window's description.  This is called by
	 * the OtItemEditorWindow.
	 */
	public void setDescriptionWidget( JTextArea descriptionWidget )
	{
		_descriptionWidget = descriptionWidget ;
	}

	/**
	 * (Re)set the title displayed by the editor window widget.  This method
	 * does not need to be called in the usual case since simply defining
	 * <tt>_title</tt> in the constructor is enough to set the title.  However,
	 * the title can be further customized after the editor is constructed
	 * by calling this method.
	 */
	public void setEditorWindowTitle( String title )
	{
		_title = title ;
		if( _otItemEditorWindow != null )
			_otItemEditorWindow.setTitle( title ) ;
	}

	/**
	 * (Re)set the description displayed by the editor window widget.  This
	 * method does not need to be called in the usual case since simply defining
	 * <tt>_description</tt> in the constructor is enough to set the title.
	 * However, the description can be further customized after the editor is
	 * constructed by calling this method.
	 */
	public void setEditorWindowDescription( String description )
	{
		_descriptionWidget.setText( description ) ;
	}

	/**
	 * Get the item currently being edited.
	 */
	public SpItem getCurrentSpItem()
	{
		return _spItem ;
	}

	/**
	 * Subclasses should override this method if they have any initialization
	 * to perform once the presentation has been set.
	 */
	protected void _init()
	{
		return ;
	}

	/**
	 * Set the item being edited.  Subclasses should implement updateWidgets to
	 * initialize the widgets in the presentation to reflect the current values
	 * of the attributes being edited.
	 *
	 * @see #_updateWidgets
	 */
	public void setup( SpItem spItem )
	{
		Assert.notNull( _pres ) ;
		_spItem = spItem ;
		update() ;
	}

	/**
	 * Called when an update to the item has occured that should be reflected
	 * in the edit window.
	 */
	public void update()
	{
		_avTab = _spItem.getTable() ;
		_updateWidgets() ;
	}

	/**
	 * This method is called when the current item is no longer being edited.
	 * Subclasses should override this method if they need to do any cleanup.
	 */
	public void cleanup(){}

	/**
	 * Subclasses must implement this method to initialize their presentation's
	 * widgets correctly based on the current item.
	 */
	protected abstract void _updateWidgets() ;

	/**
	 * A helper method that can be used to set the value of an attribute based
	 * on the string in a TextBoxWidgetExt interpreted as an integer.  If the
	 * string doesn't parse into an integer, the supplied default value is
	 * stored instead.
	 */
	public final void setAttribute( TextBoxWidgetExt tbwe , String attribute , int def )
	{
		int i = tbwe.getIntegerValue( def ) ;
		_avTab.set( attribute , i ) ;
	}

	/**
	 * A helper method that can be used to set the value of an attribute based
	 * on the string in a TextBoxWidgetExt interpreted as a double.  If the
	 * string doesn't parse into a double, the supplied default value is
	 * stored instead.
	 */
	public final void setAttribute( TextBoxWidgetExt tbwe , String attribute , double def )
	{
		double d = tbwe.getDoubleValue( def ) ;
		_avTab.set( attribute , d ) ;
	}

	/**
	 * Call the reset method of the associated TelescopePosEditor.
	 */ 
	protected void resetTPE()
	{
		if( _spItem != null )
		{
			TelescopePosEditor tpe = TpeManager.get( _spItem ) ;
			if( tpe != null )
				tpe.reset( _spItem ) ;
		}
	}
}
