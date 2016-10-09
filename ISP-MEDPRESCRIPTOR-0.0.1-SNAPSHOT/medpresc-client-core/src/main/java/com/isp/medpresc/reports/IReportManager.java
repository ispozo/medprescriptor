package com.isp.medpresc.reports;

import java.util.Properties;

/**
 * Report manager interface.
 */
public interface IReportManager {

    /**
     * Initialization of the manager.
     *
     * @param properties
     *            {@code Properties} configured.
     */
     void initialize(Properties properties);

}
