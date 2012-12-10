package com.alfresco.designer.gui.property;

import org.activiti.bpmn.model.ServiceTask;
import org.activiti.designer.util.eclipse.ActivitiUiUtil;
import org.activiti.designer.util.property.ActivitiPropertySection;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

public class PropertyAlfrescoMailTaskSection extends ActivitiPropertySection implements ITabbedPropertyConstants {

	private Text toText;
	private Text toManyText;
	private Text fromText;
	private Text subjectText;
	private Text htmlText;
	private Text nonHtmlText;
	private Text templateText;
	private Text templateModelText;

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		TabbedPropertySheetWidgetFactory factory = getWidgetFactory();
		Composite composite = factory.createFlatFormComposite(parent);
		
		toText = createControl(composite, null, false);
		createLabel(composite, "To:", toText); //$NON-NLS-1$
		toManyText = createControl(composite, toText, false);
		createLabel(composite, "To many:", toManyText); //$NON-NLS-1$
		fromText = createControl(composite, toManyText, false);
		createLabel(composite, "From:", fromText); //$NON-NLS-1$
		subjectText = createControl(composite, fromText, false);
		createLabel(composite, "Subject:", subjectText); //$NON-NLS-1$
		templateText = createControl(composite, subjectText, false);
		createLabel(composite, "Template:", templateText); //$NON-NLS-1$
		templateModelText = createControl(composite, templateText, false);
		createLabel(composite, "Template model:", templateModelText); //$NON-NLS-1$
		htmlText = createControl(composite, templateModelText, true);
		createLabel(composite, "Html:", htmlText); //$NON-NLS-1$
		nonHtmlText = createControl(composite, htmlText, true);
		createLabel(composite, "Text:", nonHtmlText); //$NON-NLS-1$
	}
	
	private Text createControl(Composite composite, Text otherTextControl, boolean multi) {
		Text textControl = null;
		FormData data = null;
		if(multi == true) {
			textControl = getWidgetFactory().createText(composite, "", SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
			data = new FormData(SWT.DEFAULT, 100);
		} else {
			textControl = getWidgetFactory().createText(composite, "", SWT.NONE);
			data = new FormData();
		}
		data.left = new FormAttachment(0, 120);
		data.right = new FormAttachment(100, 0);
		if(otherTextControl == null) {
			data.top = new FormAttachment(0, VSPACE);
		} else {
			data.top = new FormAttachment(otherTextControl, VSPACE);
		}
		textControl.setLayoutData(data);
		textControl.addFocusListener(listener);
		return textControl;
	}
	
	private CLabel createLabel(Composite composite, String labelName, Text textControl) {
		CLabel labelControl = getWidgetFactory().createCLabel(composite, labelName); //$NON-NLS-1$
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(textControl, -HSPACE);
		data.top = new FormAttachment(textControl, 0, SWT.CENTER);
		labelControl.setLayoutData(data);
		return labelControl;
	}

	@Override
	public void refresh() {
		PictogramElement pe = getSelectedPictogramElement();
		if (pe != null) {
			toText.removeFocusListener(listener);
			toManyText.removeFocusListener(listener);
			fromText.removeFocusListener(listener);
			subjectText.removeFocusListener(listener);
			templateText.removeFocusListener(listener);
			templateModelText.removeFocusListener(listener);
			htmlText.removeFocusListener(listener);
			nonHtmlText.removeFocusListener(listener);
			Object bo = getBusinessObject(pe);
			// the filter assured, that it is a EClass
			if (bo == null)
				return;

			ServiceTask mailTask = (ServiceTask)  bo;
			String to = getFieldString("mail.parameters.to", mailTask);
			toText.setText(to == null ? "" : to);
			String toMany = getFieldString("mail.parameters.to_many", mailTask);
			toManyText.setText(toMany == null ? "" : toMany);
			String from = getFieldString("mail.parameters.from", mailTask);
			fromText.setText(from == null ? "" : from);
			String subject = getFieldString("mail.parameters.subject", mailTask);
			subjectText.setText(subject == null ? "" : subject);
			String template = getFieldString("mail.parameters.template", mailTask);
			templateText.setText(template == null ? "" : template);
			String templateModel = getFieldString("mail.parameters.template_model", mailTask);
			templateModelText.setText(templateModel == null ? "" : templateModel);
			String html = getFieldString("mail.parameters.html", mailTask);
			htmlText.setText(html == null ? "" : html);
			String text = getFieldString("mail.parameters.text", mailTask);
			nonHtmlText.setText(text == null ? "" : text);
			
			toText.addFocusListener(listener);
			toManyText.addFocusListener(listener);
			fromText.addFocusListener(listener);
			subjectText.addFocusListener(listener);
			templateText.addFocusListener(listener);
			templateModelText.addFocusListener(listener);
			htmlText.addFocusListener(listener);
			nonHtmlText.addFocusListener(listener);
		}
	}

	private FocusListener listener = new FocusListener() {

		public void focusGained(final FocusEvent e) {
		}

		public void focusLost(final FocusEvent e) {
			PictogramElement pe = getSelectedPictogramElement();
			if (pe != null) {
				final Object bo = getBusinessObject(pe);
				if (bo instanceof ServiceTask) {
					DiagramEditor diagramEditor = (DiagramEditor) getDiagramEditor();
					TransactionalEditingDomain editingDomain = diagramEditor.getEditingDomain();
					ActivitiUiUtil.runModelChange(new Runnable() {
						public void run() {
							ServiceTask mailTask = (ServiceTask)  bo;
							setFieldString("mail.parameters.to", toText.getText(), mailTask);
							setFieldString("mail.parameters.to_many", toManyText.getText(), mailTask);
							setFieldString("mail.parameters.from", fromText.getText(), mailTask);
							setFieldString("mail.parameters.subject", subjectText.getText(), mailTask);
							setFieldString("mail.parameters.template", templateText.getText(), mailTask);
							setFieldString("mail.parameters.template_model", templateModelText.getText(), mailTask);
							setFieldString("mail.parameters.html", htmlText.getText(), mailTask);
							setFieldString("mail.parameters.text", nonHtmlText.getText(), mailTask);
						}
					}, editingDomain, "Model Update");
				}

			}
		}
	};

}
