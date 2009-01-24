package com.arcmind.codegen
import groovy.text.SimpleTemplateEngine

/**
 * Generates .java files from JavaClass models objects. 
 */
class XHTMLCodeGenerator implements CodeGenerator {
    List<JavaClass> classes
    /** The target output dir. Defaults to ./target */
    File rootDir = new File(".")
    String packageName //not used
    boolean debug
    SimpleTemplateEngine engine = new SimpleTemplateEngine()
    boolean use=false
    
    String oneToManyTemplate = '''
					<crank:detailListing
						detailController="#{${bean.name.unCap()}Crud.controller.children.${relationship.name}}" 
						propertyNames="${relationship.relatedClass.simplePropertyNames.join(',')}"
						parentForm="${bean.name.unCap()}Form"/>
'''

    String manyToManyTemplate = '''
                    <crank:selectMany jsfSelectManyController="#{${relationship.owner.name.unCap()}To${relationship.name.cap()}Controller}"
					    propertyNames="${relationship.relatedClass.descriptivePropertyName}" parentForm="${bean.name.unCap()}Form" />
'''
    
    String listingTemplate = '''
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="https://ajax4jsf.dev.java.net/ajax"
	xmlns:rich="http://richfaces.ajax4jsf.org/rich"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:crank="http://www.googlecode.com/crank">
<ui:composition template="/templates/layout.xhtml">
	<ui:define name="content">
		<crank:crudBreadCrumb crud="#{cruds.${bean.name.unCap()}.controller}" />
		<a4j:form id="${bean.name.unCap()}ListForm">
			<crank:listing propertyNames="${bean.propertyNames.join(',')}" parentForm="${bean.name.unCap()}ListForm" jsfCrudAdapter="#{cruds.${bean.name.unCap()}}"  />
		</a4j:form>
	</ui:define>
</ui:composition>
</html>
'''
    String formTemplate = '''
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="https://ajax4jsf.dev.java.net/ajax"
	xmlns:rich="http://richfaces.ajax4jsf.org/rich"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:crank="http://www.googlecode.com/crank">
<ui:composition template="/templates/layout.xhtml">
	<ui:define name="content">
		<crank:crudBreadCrumb crud="#{${bean.name.unCap()}Crud.controller}" />
		<c:choose>
			<c:when test='#{${bean.name.unCap()}.controller.state == "ADD"}'>
				<h:outputText value="Create ${bean.name}" styleClass="pageTitle" />
			</c:when>
			<c:otherwise>
				<h:outputText
					value="Edit ${bean.name} #{${bean.name.unCap()}Crud.controller.entity.${bean.descriptivePropertyName}}"
					styleClass="pageTitle" />
			</c:otherwise>
		</c:choose>
		<a4j:form id="${bean.name.unCap()}Form">
			<rich:messages errorClass="pageErrorMessage" />
			<crank:form parentForm="${bean.name.unCap()}Form"
				crud="#{${bean.name.unCap()}Crud.controller}" propertyNames="${bean.propertyNames.join(',')}">
				${formBody}
			</crank:form>
		</a4j:form>
	</ui:define>
</ui:composition>
</html>
'''

	def doProcess(String fileNameSuffix, String template) {
        for (JavaClass bean in classes) {
        	
        	if (debug) println "Writing ${bean.name} listing xhtml"
            def binding = [bean:bean, formBody:generateBody(bean)]
            String templateOutput = engine.createTemplate(template).make(binding).toString()
            File webappRootDir = new File(rootDir, "src/main/webapp/pages/crud")
            File listingFile = new File (webappRootDir, bean.name.unCap() + fileNameSuffix)
        	listingFile.newWriter().withWriter{BufferedWriter writer->
            	writer.write(templateOutput)
            }
        }
	
	}
	public void process() {
		if (use) {
			doProcess("Listing.xhtml", listingTemplate)
			doProcess("Form.xhtml", formTemplate)
		}
    }

	
	String generateBody(JavaClass bean) {
		StringBuilder builder = new StringBuilder()
		bean.relationships.each {Relationship relationship ->
			if (relationship.type == RelationshipType.ONE_TO_MANY) {
				builder << engine.createTemplate(oneToManyTemplate).make([bean:bean, relationship:relationship]).toString()
			} else if (relationship.type == RelationshipType.MANY_TO_MANY) {
                builder << engine.createTemplate(manyToManyTemplate).make([bean:bean, relationship:relationship]).toString()
            }
		}
		builder.toString()
	}
}