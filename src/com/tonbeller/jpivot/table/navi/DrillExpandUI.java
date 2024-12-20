/*
 * ====================================================================
 * This software is subject to the terms of the Common Public License
 * Agreement, available at the following URL:
 *   http://www.opensource.org/licenses/cpl.html .
 * Copyright (C) 2003-2004 TONBELLER AG.
 * All Rights Reserved.
 * You must accept the terms of that agreement to use this software.
 * ====================================================================
 *
 * 
 */
package com.tonbeller.jpivot.table.navi;

import java.io.Serializable;

import org.w3c.dom.Element;

import com.tonbeller.jpivot.core.ModelChangeEvent;
import com.tonbeller.jpivot.core.ModelChangeListener;
import com.tonbeller.jpivot.olap.model.Member;
import com.tonbeller.jpivot.olap.model.Position;
import com.tonbeller.jpivot.table.AxisBuilder;
import com.tonbeller.jpivot.table.SpanBuilder;
import com.tonbeller.jpivot.table.SpanBuilderDecorator;
import com.tonbeller.jpivot.table.TableComponent;
import com.tonbeller.jpivot.table.TableComponentExtensionSupport;
import com.tonbeller.jpivot.table.span.Span;
import com.tonbeller.wcf.component.RendererParameters;
import com.tonbeller.wcf.controller.Dispatcher;
import com.tonbeller.wcf.controller.DispatcherSupport;
import com.tonbeller.wcf.controller.RequestContext;
import com.tonbeller.wcf.controller.RequestListener;
import com.tonbeller.wcf.scroller.Scroller;
import com.tonbeller.wcf.utils.DomUtils;

/**
 * adds expand-node, collapse-node functionality.
 * Decorates the controller (dispatcher) and view (renderer) of the table component.
 * 
 * @author av
 */
public abstract class DrillExpandUI extends TableComponentExtensionSupport implements ModelChangeListener, Serializable {
  boolean available;
  boolean renderActions;

  Dispatcher dispatcher = new DispatcherSupport();

  public void initialize(RequestContext context, TableComponent table) throws Exception {
    super.initialize(context, table);
    table.getOlapModel().addModelChangeListener(this);

    // does the underlying data model support drillexpand?
    available = initializeExtension();

    // extend the controller 
    table.getDispatcher().addRequestListener(null, null, dispatcher);

    // add some decorators via table.get/setRenderer
    AxisBuilder rab = table.getRowAxisBuilder();
    DomDecorator rhr = new DomDecorator(rab.getSpanBuilder());
    rab.setSpanBuilder(rhr);

    AxisBuilder cab = table.getColumnAxisBuilder();
    DomDecorator chr = new DomDecorator(cab.getSpanBuilder());
    cab.setSpanBuilder(chr);
  }

  public void startBuild(RequestContext context) {
    super.startBuild(context);
    renderActions = RendererParameters.isRenderActions(context);
    if (renderActions)
      dispatcher.clear();
  }

  class DomDecorator extends SpanBuilderDecorator {

    DomDecorator(SpanBuilder delegate) {
      super(delegate);
    }

    public Element build(SBContext sbctx, Span span, boolean even) {
      Element parent = super.build(sbctx, span, even);

      if (!enabled || !renderActions || !available)
        return parent;

      String id = DomUtils.randomId();
      if (canExpand(span)) {
        Element elem = table.insert("drill-expand", parent);
        elem.setAttribute("id", id);
        elem.setAttribute("img", getExpandImage());
        dispatcher.addRequestListener(id, null, new ExpandHandler(span));
      } else if (canCollapse(span)) {
        Element elem = table.insert("drill-collapse", parent);
        elem.setAttribute("id", id);
        elem.setAttribute("img", getCollapseImage());
        dispatcher.addRequestListener(id, null, new CollapseHandler(span));
      } else {
        Element elem = table.insert("drill-other", parent);
        elem.setAttribute("img", getOtherImage());
      }
      return parent;
    }
  }

  class ExpandHandler implements RequestListener {
    Span span;
    ExpandHandler(Span span) {
      this.span = span;
    }
    public void request(RequestContext context) throws Exception {
      Scroller.enableScroller(context);
      if (canExpand(span)) // back button etc
        expand(span);
    }
  }

  class CollapseHandler implements RequestListener {
    Span span;
    CollapseHandler(Span span) {
      this.span = span;
    }
    public void request(RequestContext context) throws Exception {
      Scroller.enableScroller(context);
      if (canCollapse(span)) // back button etc
        collapse(span);
    }
  }

  /** @return true if extension is available */
  protected abstract boolean initializeExtension();
  protected abstract boolean canExpand(Span span);
  protected abstract void expand(Span span);
  protected abstract boolean canCollapse(Span span);
  protected abstract void collapse(Span span);
  protected abstract String getExpandImage();
  protected abstract String getCollapseImage();
  protected abstract String getOtherImage();

  public boolean isAvailable() {
    return available;
  }

  /** utility */
  static int indexOf(Object[] array, Object obj) {
    for (int i = 0; i < array.length; i++)
      if (array[i] == obj)
        return i;
    return -1;
  }

  /**
   * true, s represents a member that is in the original result position.
   * false, if s is not a member or if s is a member, that had been added to
   * show the parents of the member
   */
  static boolean positionContainsMember(Span s) {
    if (!s.isMember())
      return false;
    Member m = (Member) s.getMember().getRootDecoree();
    Position p = (Position) s.getPosition().getRootDecoree();
    return indexOf(p.getMembers(), m) >= 0;
  }

  public void modelChanged(ModelChangeEvent e) {
  }

  public void structureChanged(ModelChangeEvent e) {
    available = initializeExtension();
    dispatcher.clear();
  }

}
