package org.orcid.core.manager.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.orcid.core.manager.OrcidProfileCleaner;
import org.orcid.core.tree.TreeCleaner;
import org.orcid.core.tree.TreeCleaningDecision;
import org.orcid.core.tree.TreeCleaningStrategy;
import org.orcid.jaxb.model.message.OrcidIdBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrcidProfileCleanerImpl implements OrcidProfileCleaner, TreeCleaningStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrcidProfileCleanerImpl.class);

    @Override
    public void clean(Object object) {
        TreeCleaner treeCleaner = new TreeCleaner();
        treeCleaner.clean(object, this);
    }

    @Override
    public TreeCleaningDecision needsStripping(Object obj) {
        if (obj == null) {
            return TreeCleaningDecision.DEFAULT;
        }
        if (hasBlankProperty(obj, "content") || (hasBlankProperty(obj, "value") && !(obj instanceof OrcidIdBase))) {
            return TreeCleaningDecision.CLEANING_REQUIRED;
        }
        return TreeCleaningDecision.DEFAULT;
    }

    private boolean hasBlankProperty(Object obj, String propertyName) {
        try {
            Method getter = obj.getClass().getMethod("get" + StringUtils.capitalize(propertyName));
            Object propertyValue = getter.invoke(obj);
            if (propertyValue == null) {
                return true;
            }
            if (String.class.isAssignableFrom(propertyValue.getClass())) {
                return StringUtils.isBlank((String) propertyValue);
            }
        } catch (SecurityException e) {
            LOGGER.error("Problem doing reflection while cleaning", e);
        } catch (NoSuchMethodException e) {
            return false;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Problem doing reflection while cleaning", e);
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
            LOGGER.error("Problem doing reflection while cleaning", e);
        }
        return false;
    }

}
