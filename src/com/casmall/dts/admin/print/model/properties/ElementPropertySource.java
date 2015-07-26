package com.casmall.dts.admin.print.model.properties;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.casmall.dts.admin.print.model.BaseElement;
import com.casmall.dts.admin.print.model.annotations.PropertyDescription;

/**
 * property sheet 备己
 * 
 * @author oberak
 */
public class ElementPropertySource implements IPropertySource {
	protected static Log logger = LogFactory.getLog(ElementPropertySource.class);
	private BaseElement element;

	public ElementPropertySource(BaseElement element) {
		this.element = element;
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();

		Class<? extends BaseElement> cls = element.getClass();

		Field[] fields = cls.getFields();
		try {
			for (Field field : fields) {
				PropertyDescription annotation = field.getAnnotation(PropertyDescription.class);
				if (annotation != null) {
					PropertyDescriptor descriptor = null;
					if(annotation.type().equals(FontData.class)){
						descriptor = new FontPropertyDescriptor(field.getName(), annotation.label());
						descriptor.setLabelProvider(new LabelProvider() {
							@Override
							public String getText(Object element) {
								FontData fontData = new FontData((String) element);
								StringBuffer label = new StringBuffer();
								label.append(fontData.getName()).append(" ").append(fontData.getHeight());
								return label.toString();
							}
						});
					}else if(annotation.type().equals(Color.class)){
						descriptor = new ColorPropertyDescriptor(field.getName(), annotation.label());
					} else if (annotation.value().length > 1) {
						// COMBOBOX
						descriptor = new ComboBoxPropertyDescriptor(field.getName(), annotation.label(), annotation.value());
					} else {
						descriptor = new TextPropertyDescriptor(field.getName(), annotation.label());
					}

					descriptor.setCategory(annotation.category());
					properties.add(descriptor);
				}
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("getPropertyDescriptors error - " + e.getMessage());
			}
			e.printStackTrace();
		}

		return properties.toArray(new IPropertyDescriptor[0]);
	}// getPropertyDescriptors

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		Class<? extends BaseElement> cls = element.getClass();
		Field field;
		try {
			field = cls.getField((String) id);
			PropertyDescription annotation = field.getAnnotation(PropertyDescription.class);
			if (annotation != null) {
				invokeSet(element, annotation.field(), value);
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("setPropertyValue error - " + e.getMessage());
			}
			e.printStackTrace();
		}
	}// setPropertyValue

	@Override
	public Object getPropertyValue(Object id) {
		Class<? extends BaseElement> cls = element.getClass();
		Field field;
		try {
			field = cls.getField((String) id);
			PropertyDescription annotation = field.getAnnotation(PropertyDescription.class);

			if (annotation != null) {
				Object rtn = invokeGet(element, annotation.field());

				if (rtn == null) {
					if (logger.isErrorEnabled()) {
						logger.error("invokeGet value is nul - " + annotation.field());
					}
					return "";
				}
				if (annotation.value().length > 1) {
					// COMBO 老 版快绰 index return
					return rtn;
				}
				if (rtn instanceof Integer || rtn instanceof Double) {
					return String.valueOf(rtn);
				} else if (rtn instanceof FontData) {
					return ((FontData) rtn).toString();
				} else if (rtn instanceof Color) {
					return ((Color) rtn).getRGB();
				} else {
					return rtn.toString();
				}
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("getPropertyValue error - " + e.getMessage());
			}
			e.printStackTrace();
		}
		return null;
	}// getPropertyValue

	/**
	 * Getter value get
	 * 
	 * @param obj
	 * @param attr
	 * @return
	 */
	public Object invokeGet(Object obj, String attr) {
		try {
			String methodName = "get" + attr.substring(0, 1).toUpperCase() + attr.substring(1);
			Method method = obj.getClass().getMethod(methodName, (Class[]) null);
			return method.invoke(obj, (Object[]) null);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("invokeGet error - " + e.getMessage());
			}
			e.printStackTrace();
		}
		return null;
	}// invokeGet

	/**
	 * Setter value set
	 * 
	 * @param obj
	 * @param attr
	 * @param value
	 */
	public void invokeSet(Object obj, String attr, Object value) {
		try {
			String methodName = "set" + attr.substring(0, 1).toUpperCase() + attr.substring(1);

			Method[] methods = obj.getClass().getMethods();
			Method method = null;
			for (Method m : methods) {
				if (methodName.equals(m.getName()) && m.getParameterTypes().length == 1) {
					method = m;
					break;
				}
			}
			if (method == null) {
				if (logger.isErrorEnabled())
					logger.error("invokeSet Error: not found method - " + methodName);
				return;
			}
			Class<?>[] paramType = method.getParameterTypes();
			Object[] param = new Object[1];
			if (double.class.getName().equals(paramType[0].getName())) {
				param[0] = Double.valueOf((String) value);
			} else if (int.class.getName().equals(paramType[0].getName())) {
				if (value instanceof Integer) {
					param[0] = value;
				} else {
					param[0] = Integer.parseInt((String) value);
				}
			} else if (Color.class.getName().equals(paramType[0].getName())) {
				param[0] = new Color(null, (RGB) value);
			} else {
				param[0] = value;
			}
			
			method.invoke(obj, param);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("invokeSet error - ["+attr+"/"+value+"] "+ e.getMessage());
			}
			e.printStackTrace();
		}
		return;
	}// invokeSet
}// class