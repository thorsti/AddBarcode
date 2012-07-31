package addBarcode;

import java.io.IOException;

public class AddBarcode {

	/**
	 * @param args
	 * @author Thorsten Jezierski
	 * @version 0.2
	 * @description Main Class to add Barcodes on PDF File
	 * Returncodes:
	 * -1 Just one Parameter that is not -h was given
	 * -2 Paramter Count is invalid
	 */
	public static void main(String[] args) {
		/**
		 * Arguments must be:
		 * WorkingFolder Filename Barcode X,Y Scale
		 * Arguments that must not been set
		 * Scale
		 *  		
		 */
		//Begin Declaration of Argument Vars
		String workingFolder = "";
		String filename = "";
		String barcode = "";
		double xCoord = 0.0;
		double yCoord = 0.0;
		double scale = 0.15;
		
		switch(args.length){
		case 1:
			if(args[0].compareTo("-h") == 0){
				showHelp();
			}else{
				System.out.println("Unbekannter Parameter");
				System.exit(-1);
			}
			break;
			
		case 5:
		case 6:
			
			workingFolder = args[0];
			filename = args[1];
			barcode = args[2];
			xCoord = Integer.parseInt(args[3]);
			yCoord = Integer.parseInt(args[4]);
			if(args.length == 6){
				scale = Double.parseDouble(args[5]);
			}
			break;
		default:
			System.out.println("Ungueltige Parameterzahl. Erwartet 6[7] uebergeben: " + args.length);
			System.exit(-2);
			
		}
		
		/**
		 * Create the Barcode first using Bytestream
		 */
		System.out.println("Creating Barcode");
		Barcode code = new Barcode(barcode);
		System.out.println("Creating PDF");
		PDF file = new PDF(workingFolder,filename,xCoord,yCoord,scale,code);
		
		try {
			code.createBarcodeImage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		file.loadPDFinPVF();
		file.add();
	}
	
	public static void showHelp(){
		//TODO Write Help
	}
	

}
