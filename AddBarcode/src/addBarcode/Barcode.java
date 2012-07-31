package addBarcode;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

public class Barcode {

	private String code;
	private String rawCode;
	private Code128Bean bean = new Code128Bean();
	private BitmapCanvasProvider canvas;
	final int dpi = 600;
	
	
	//Constructors
	public Barcode(){
		this("KBK00000DUMMY00000-");
	}
	
	public Barcode(String data){
		this.rawCode = data;
		if(data.contains("_")){
			String[] tmp = data.split("_");
			this.code = tmp[0];
		}else{
			this.code = data;
		}
		this.code = data;
		this.bean.setModuleWidth(UnitConv.in2mm(4.0f / dpi)); //makes the narrow bar 
        this.bean.setBarHeight(4.0);
        this.bean.setFontSize(2.5);
        this.bean.setQuietZone(1.0);
        this.bean.doQuietZone(true);        
        this.canvas = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_BYTE_GRAY,false, 0);
	}
	
	public void createBarcodeImage() throws IOException
	{
		this.bean.generateBarcode(canvas,this.code);	    
		//Signal end of generation
		this.canvas.finish();
	}
	
	//Get and Set Methods
	/**
	 * 
	 * @return String Barcode
	 */
	public String getRawCode(){
		return this.rawCode;
	}
	
	public String getCode(){
		return this.code;
	}
	
	/**
	 * 
	 * @param data Barcode
	 * 
	 * If provided code is not 16 Characters long it will be shortened:
	 * If it Contains - these will be stripped
	 * If it doesn't contain - a 16 characters substring will be created
	 * If an Exception will be thrown
	 */
	
	public void setCode(String data) throws Exception{
		if(data.length() == 16){
			this.code = data;
		}else if(data.length() > 16){
			if(data.contains("-")){
			this.code = this.stripString(data,"-");
			}
			
		}else{
			throw new Exception("Provided Barcode is too Short");
		}
	}
	
	/**
	 * 
	 * @return BitmapCanvasProvider Returns the canvas Object 
	 */
	
	public BitmapCanvasProvider getCanvasObject(){
		return this.canvas;		//Simply return it
	}
	
	/**
	 * 
	 * @param input String to be stripped
	 * @param needle What do you want to Strip
	 * @return Stripped String
	 */
	
	private String stripString(String input,String needle){
		String strippedContent = "";
		strippedContent = input.replace(needle, "");
		return strippedContent;
	}
	
	
}
