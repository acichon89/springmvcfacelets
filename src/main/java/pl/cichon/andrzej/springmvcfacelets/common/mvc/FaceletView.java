package pl.cichon.andrzej.springmvcfacelets.common.mvc;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class FaceletView extends AbstractUrlBasedView {

	private Lifecycle facesLifecycle;

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		facesLifecycle = createFacesLifecycle();
	}

	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response)	throws Exception {
		FacesContext facesContext = createFacesContext(request, response);
		populateRequestMap(facesContext, model);
	
		FaceletView.notifyBeforeListeners(PhaseId.RESTORE_VIEW, facesLifecycle, facesContext);
	
		ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
	
		viewHandler.initView(facesContext);
	
		UIViewRoot viewRoot = viewHandler.createView(facesContext, getUrl());
		Assert.notNull(viewRoot, "A JSF view could not be created for " + getUrl());
		viewRoot.setLocale(RequestContextUtils.getLocale(request));
		viewRoot.setTransient(true);
	
		facesContext.setViewRoot(viewRoot);
	
		FaceletView.notifyAfterListeners(PhaseId.RESTORE_VIEW, facesLifecycle, facesContext);
	
		facesContext.setViewRoot(viewRoot);
		facesContext.renderResponse();
		try {
			FaceletView.notifyBeforeListeners(PhaseId.RENDER_RESPONSE, facesLifecycle, facesContext);
			logger.debug("Asking view handler to render view");
			facesContext.getApplication().getViewHandler().renderView(facesContext, viewRoot);
			FaceletView.notifyAfterListeners(PhaseId.RENDER_RESPONSE, facesLifecycle, facesContext);
		} 
		catch (IOException e) {
			throw new FacesException("An I/O error occurred during view rendering", e);
		} finally {
			logger.debug("View rendering complete");
			facesContext.responseComplete();
			facesContext.release();
		}
	}

	private void populateRequestMap(FacesContext facesContext, Map model) {
		Iterator i = model.keySet().iterator();
		while (i.hasNext()) {
			String key = i.next().toString();
			facesContext.getExternalContext().getRequestMap().put(key, model.get(key));
		}
	}

	private FacesContext createFacesContext(HttpServletRequest request, HttpServletResponse response) {
		FacesContextFactory facesContextFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
		return facesContextFactory.getFacesContext(getServletContext(), request, response, facesLifecycle);
	}

	private Lifecycle createFacesLifecycle() {
		LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
		return lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
	}

	public static void notifyAfterListeners(PhaseId phaseId,
			Lifecycle lifecycle, FacesContext context) {
		PhaseEvent afterPhaseEvent = new PhaseEvent(context, phaseId, lifecycle);
		for (int i = 0; i < lifecycle.getPhaseListeners().length; i++) {
			PhaseListener listener = lifecycle.getPhaseListeners()[i];
			if (listener.getPhaseId() == phaseId
					|| listener.getPhaseId() == PhaseId.ANY_PHASE) {
				listener.afterPhase(afterPhaseEvent);
			}
		}
	}

	public static void notifyBeforeListeners(PhaseId phaseId,
			Lifecycle lifecycle, FacesContext context) {
		PhaseEvent beforePhaseEvent = new PhaseEvent(context, phaseId,
				lifecycle);
		for (int i = 0; i < lifecycle.getPhaseListeners().length; i++) {
			PhaseListener listener = lifecycle.getPhaseListeners()[i];
			if (listener.getPhaseId() == phaseId
					|| listener.getPhaseId() == PhaseId.ANY_PHASE) {
				listener.beforePhase(beforePhaseEvent);
			}
		}
	}

}
