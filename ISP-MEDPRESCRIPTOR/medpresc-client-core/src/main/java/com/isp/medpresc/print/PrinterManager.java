package com.isp.medpresc.print;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;



/**
 * Printer manager.
 * 
 * @author ispozo
 *
 */
public class PrinterManager {

    /**
     * Logger.
     */
    private static Logger LOG = Logger.getLogger(PrinterManager.class);

    /**
     * Prints the file with the given filename in the path.
     * 
     * @param filenamePath
     *            File name.
     * @param copies Number of copies.
     * @throws PrintException
     *             in case of error printing.
     * @throws IOException if file not found.
     * @throws PrinterException printing the file.
     */
    public static void print(String filenamePath, int copies) throws IOException, PrinterException {
       
        File file = new File(filenamePath);
        PDDocument document = PDDocument.load(file);
        
        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
        if (printService == null) {
            LOG.warn("WARNING!!! No default printer found for " + filenamePath);
            return;
        }
        LOG.debug("Printing file :" + filenamePath + "on default printer :" + printService.getName());
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));
        job.setPrintService(printService);
        job.setJobName(file.getName());
        job.setCopies(copies);
        job.print();
    }
}
