package com.isp.medpresc.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import net.sf.jasperreports.engine.JRParameter;

/**
 * Report manager for MUFACE prescriptions.
 * 
 * @author ispozo
 *
 */
public class MufaceReportManager extends AReportManager {
    /**
     * Logger.
     */
    private static Logger LOG = Logger.getLogger(MufaceReportManager.class);
    /**
     * MUFACE template path.
     */
    private static final String PROPERTY_KEY_MUFACE_TEMPLATE_FILE_PATH = "MufaceTemplateFilePath";
    /**
     * MUFACE template file name.
     */
    private static final String PROPERTY_KEY_MUFACE_TEMPLATE_FILE_NAME = "MufaceTemplateFileName";

    /**
     * MUFACE generated doc name.
     */
    private static final String PROPERTY_KEY_MUFACE_DOC_NAME = "MufaceDocName";

    /**
     * Constructor. Initializes the manager.
     * 
     * @param context
     *            Properties.
     */
    public MufaceReportManager(Properties context) {
        context.setProperty(PROPERTY_KEY_DOC_NAME, context.getProperty(PROPERTY_KEY_MUFACE_DOC_NAME));
        context.setProperty(PROPERTY_KEY_REPORT_TEMPLATE_NAME,
                context.getProperty(PROPERTY_KEY_MUFACE_TEMPLATE_FILE_NAME));
        context.setProperty(PROPERTY_KEY_REPORT_TEMPLATE_PATH,
                context.getProperty(PROPERTY_KEY_MUFACE_TEMPLATE_FILE_PATH));
        super.initialize(context);
    }

    /**
     * Builds 'Muface' prescription report.
     *
     * @param selectedFlight
     *            Selected flight.
     * @param status
     *            {@code InvoicingStatus}.
     * @return Path of the created file.
     * @throws Exception
     *             in case of error.
     */
    public String createMufacePrescriptionPdf(String prescription) throws Exception {
        LOG.info("Creating PDF for prescription " + prescription);
        String path = null;

        Map<String, Object> paramList = new HashMap<String, Object>();
        paramList.put("PRESCRIPTION_PARAM", prescription);
        paramList.put(JRParameter.REPORT_LOCALE, Locale.getDefault());
        // paramList.put(JRParameter.REPORT_RESOURCE_BUNDLE, ResourceBundle.getBundle(DEFAULT_CUSTOM_REPORT_BUNDLE));
        path = execute(paramList);
        return path;
    }

}
