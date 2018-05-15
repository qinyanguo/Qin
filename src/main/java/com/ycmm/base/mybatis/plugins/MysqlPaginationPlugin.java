package com.ycmm.base.mybatis.plugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.PrimitiveTypeWrapper;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;


public class MysqlPaginationPlugin extends PluginAdapter {
	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		// add field, getter, setter for limit clause
		addLimit(topLevelClass, introspectedTable, "startIndex");
		addLimit(topLevelClass, introspectedTable, "pageSize");
		return super.modelExampleClassGenerated(topLevelClass,
				introspectedTable);
	}
	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
			XmlElement element, IntrospectedTable introspectedTable) {
//		XmlElement isParameterPresenteElemen = (XmlElement) element
//				.getElements().get(element.getElements().size() - 1);
		XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
		isNotNullElement.addAttribute(new Attribute("test", "startIndex != null and startIndex>=0")); //$NON-NLS-1$ //$NON-NLS-2$
//		isNotNullElement.addAttribute(new Attribute("compareValue", "0")); //$NON-NLS-1$ //$NON-NLS-2$
		isNotNullElement.addElement(new TextElement(
				"limit #{startIndex} , #{pageSize}"));
//		isParameterPresenteElemen.addElement(isNotNullElement);
		element.addElement(isNotNullElement);
		return super.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element,
				introspectedTable);
	}
	private void addLimit(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable, String name) {
		CommentGenerator commentGenerator = context.getCommentGenerator();
		Field field = new Field();
		field.setVisibility(JavaVisibility.PROTECTED);
//		field.setType(FullyQualifiedJavaType.getIntInstance());
		field.setType(PrimitiveTypeWrapper.getIntegerInstance());
		field.setName(name);
//		field.setInitializationString("-1");
		commentGenerator.addFieldComment(field, introspectedTable);
		topLevelClass.addField(field);
		char c = name.charAt(0);
		String camel = Character.toUpperCase(c) + name.substring(1);
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("set" + camel);
		method.addParameter(new Parameter(PrimitiveTypeWrapper.getIntegerInstance(), name));
		method.addBodyLine("this." + name + "=" + name + ";");
		commentGenerator.addGeneralMethodComment(method, introspectedTable);
		topLevelClass.addMethod(method);
		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(PrimitiveTypeWrapper.getIntegerInstance());
		method.setName("get" + camel);
		method.addBodyLine("return " + name + ";");
		commentGenerator.addGeneralMethodComment(method, introspectedTable);
		topLevelClass.addMethod(method);
	}
	/**
	 * This plugin is always valid - no properties are required
	 */
	public boolean validate(List<String> warnings) {
		return true;
	}
	
	
	
	
	public static void main(String[] args) {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		File configFile = new File("E:/Workspaces/QuDong/WebRoot/WEB-INF/gen/mybatis-gen.xml");
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config;
		try {
			config = cp.parseConfiguration(configFile);

			DefaultShellCallback callback = new DefaultShellCallback(overwrite);
			MyBatisGenerator myBatisGenerator;
			try {
				myBatisGenerator = new MyBatisGenerator(config, callback,warnings);
				myBatisGenerator.generate(null);
				System.out.println(warnings);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLParserException e) {
			e.printStackTrace();
		}
	}

}