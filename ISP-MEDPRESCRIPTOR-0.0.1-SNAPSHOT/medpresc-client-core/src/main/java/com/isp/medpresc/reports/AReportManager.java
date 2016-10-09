package com.isp.medpresc.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Abstract class that may be used as starting point to implement a report manager.
 */
public abstract class AReportManager implements IReportManager {

    /**
     * Property key. Document name.
     */
    protected static final String PROPERTY_KEY_DOC_NAME = "docName";

    /**
     * Property key. Specific relative template path.
     */
    protected static final String PROPERTY_KEY_REPORT_TEMPLATE_PATH = "templateFilePath";

    /**
     * Property key. Specific template name.
     */
    protected static final String PROPERTY_KEY_REPORT_TEMPLATE_NAME = "templateFileName";

    /**
     * PDF extension.
     */
    private static final Object PDF_SUFFIX = ".pdf";

    /**
     * Root path for templates.
     */
    protected static final String PROPERTY_KEY_ROOT_PATH = "rootPath";

    /**
     * Path to save reports.
     */
    protected static final String PROPERTY_KEY_REPO_FILE_PATH = "repoFilePath";

    /**
     * Root path for reports.
     */
    private String rootPath;

    /**
     * Path of the repository for generated files.
     */
    private String repoFilePath;

    /**
     * Configured properties for this manager.
     */
    private Properties properties;

    /**
     * Template's name.
     */
    private String templateName;

    /**
     * Document's name.
     */
    private String docName;

    /**
     * Relative template's path.
     */
    private String templatePath;

    @Override
    public void initialize(Properties properties) {

        this.properties = properties;
        if (this.properties == null) {
            throw new NullPointerException("ERROR!! null properties for report manager");
        }

        this.rootPath = properties.getProperty(PROPERTY_KEY_ROOT_PATH);
        if (this.rootPath == null) {
            throw new NullPointerException("ERROR!! null root path.");
        }

        this.repoFilePath = properties.getProperty(PROPERTY_KEY_REPO_FILE_PATH);
        if (this.repoFilePath == null) {
            throw new NullPointerException("ERROR!! null repo file.");
        }

        this.templateName = properties.getProperty(PROPERTY_KEY_REPORT_TEMPLATE_NAME);
        if (this.templateName == null) {
            throw new NullPointerException("ERROR!! null template name.");
        }

        this.docName = properties.getProperty(PROPERTY_KEY_DOC_NAME);
        if (this.docName == null) {
            throw new NullPointerException("ERROR!! null docName for report manager");
        }

        this.templatePath = properties.getProperty(PROPERTY_KEY_REPORT_TEMPLATE_PATH);
        if (this.templatePath == null) {
            throw new NullPointerException("ERROR!! null template path");
        }

    }

    /**
     * Returns the root path for reports.
     * 
     * @return Root path for reports.
     */
    public String getRootPath() {
        return rootPath;
    }

    /**
     * Returns the path of the repository for generated files.
     *
     * @return Path of the repository.
     */
    public String getRepoFilePath() {
        return repoFilePath;
    }

    /**
     * Returns absolute template's path.
     * 
     * @return Absolute template's path.
     */
    public String getTemplatePath() {
        return rootPath + templatePath;
    }

    /**
     * Execute the report with the given input parameters.
     *
     * @param params
     *            Input parameters for this report.
     * @throws EPException
     *             in case of error generating the report.
     * @throws NullPointerException
     *             {@code null} required properties (template and doc name).
     */
    public final String execute(Map<String, Object> params) {

        String templateFilePath = rootPath + templatePath + templateName;

        // Path to save the document.
        StringBuilder savePath = new StringBuilder(repoFilePath).append("\\").append(docName).append(PDF_SUFFIX);

        // Adds the root dir.
        params.put("ROOT_DIR", rootPath);
        params.put("SUBREPORT_DIR", rootPath + templatePath);

        JasperReport report = null;
        try {
            File jasperReport = null;
            // Loads template as a resource or as an absolute file.
            URL url = this.getClass().getResource(templateFilePath);
            if (url != null) {
                jasperReport = new File(url.toURI());
            } else {
                jasperReport = new File(templateFilePath);
            }
            report = (JasperReport) JRLoader.loadObject(jasperReport);
            byte[] pdf = JasperRunManager.runReportToPdf(report, params);
            FileOutputStream fos = new FileOutputStream(savePath.toString());
            fos.write(pdf);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return savePath.toString();

    }

}
