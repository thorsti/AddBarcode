package addBarcode;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import com.pdflib.PDFlibException;
import com.pdflib.pdflib;

public class PDF {
	
	private pdflib p;
	private Barcode code;
	private String searchpath;
	private String filename;
		private double xCoord;
	private double yCoord;
	private double scale;
	private BitmapCanvasProvider canvas;
	
	private int doc;
	private int page;
	
	
	//Constructors
	public PDF(){
	
	}
	
	public PDF(String searchPath, String filename, double xCoord, double yCoord, double scale, Barcode code){
		this.searchpath = searchPath;
		try{
			this.p = new pdflib();
			this.p.set_parameter("SearchPath", searchPath);
	        this.p.set_parameter("errorpolicy", "return");
			System.out.println("PDF Lib");
		}catch(PDFlibException pdf){
			System.out.println(pdf.getMessage());
		}
		this.filename = filename;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.scale = scale;
		
		this.code = code;
		
		this.canvas = this.code.getCanvasObject();
		
	
	}
	
	/*
	 * Memo: Nach der Pflicht jetzt die Kür...
	 * PDFs als Stream öffnen :)
	 */
	
	public void loadPDFinPVF(){
		
		FileInputStream fis = null;
		byte[] pdfData = null;
		
		try{
		fis = new FileInputStream(this.searchpath + this.filename);
		}catch(FileNotFoundException f){
			System.out.println("Datei konnte nicht gefunden werden: " + this.searchpath + this.filename);
			System.exit(-1);
		}
		try{
			pdfData = new byte[fis.available()];
		}catch(IOException io){
			System.out.println(io.getMessage());
			System.exit(-1);
		}
		try {
			fis.read(pdfData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			this.p.create_pvf("/pvf/pdf.pdf", pdfData, "");
		} catch (PDFlibException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Loading in Virtual File");
		//Should be loaded ;)
		
	}
	
	public void add(){
		System.out.println("Begin Adding Barcode");
		int modifyPage = 0;
	    BufferedImage barcodeImage = this.canvas.getBufferedImage();    		    
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try{
		ImageIO.write(barcodeImage, "png", baos);
		}catch(IOException io){
			System.out.println(io.getMessage());
			System.exit(-1);
		}
		
		byte[] imageBytes = baos.toByteArray();
		
		System.out.println("Image loaded in ByteArray");
		
		try{
		this.p.create_pvf("/pvf/barcode.png", imageBytes, "");
		this.p.begin_document(this.searchpath +this.code.getRawCode() +".pdf", "");
		
		this.doc = this.p.open_pdi_document("/pvf/pdf.pdf","");
		if(this.doc == -1){
			System.out.println("Cant open File");
		}
		System.out.println("PDF Handlin begins her, Files open");
		//TODO Insert Barcode Adding here
		
		System.out.println("Loading in Virtual ImageFile");
		
		System.out.println("Checking Pagesize");
		int pagesize = (int) this.p.pcos_get_number(this.doc,"length:pages");
		if(pagesize == 1 || pagesize == 2){
			modifyPage = 1;
		}else{
			modifyPage = pagesize;
		}
		System.out.println("Checked");
		
		//Load every Page and fit in new Document :)
		
		for(int i = 1; i <= pagesize; i++){ // Alle uninteressanten Seiten durchparsen
	        
			System.out.println("Open Page");
        	this.page = this.p.open_pdi_page(this.doc,i,"");
        	
        	if(this.page == -1){
        		System.out.println("Error loading Page");
        	}
        	this.p.begin_page_ext(10,10,"");   	
        	this.p.fit_pdi_page(this.page, 0,0, "adjustpage"); 
        	System.out.println("");
            if(i == modifyPage){
            	int img = this.p.load_image("auto", "/pvf/barcode.png", "");
            	if(img == -1){
            		System.out.println("Error loading Image");
            	}
            	System.out.println("Insert Barcode");
            	this.p.fit_image(img, this.xCoord, this.yCoord,"scale="+this.scale);
            	this.p.close_image(img);
            }
            this.p.close_pdi_page(this.page);
            this.p.end_page_ext("");
         }
		this.p.close_pdi_document(this.doc);
		this.p.delete_pvf("/pvf/barcode.png");
		this.p.delete_pvf("/pvf/pdf.pdf");
		this.p.end_document("");
		}catch(PDFlibException pdf){
			System.out.println(pdf.getMessage());
		}
	}
	
	
}
