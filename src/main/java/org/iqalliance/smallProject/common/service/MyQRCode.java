package org.iqalliance.smallProject.common.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class MyQRCode {

	
	/*
	 * 创建一个二维码图片存储于本地
	 */
	public static boolean saveAsQrCode(OutputStream outputStream,String content,int qrCodeSize,String imageFormat) throws WriterException, IOException {
		
		//设置二维码纠错级别ＭＡＰ
		Hashtable<EncodeHintType,Object> hintMap = new Hashtable<EncodeHintType,Object>();  
		hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);  // 矫错级别
		
		//创建一个二维码生成器
		QRCodeWriter qrcodeWriter = new QRCodeWriter();
		//创建一个比特矩阵
		BitMatrix bitMatrix = qrcodeWriter.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize,hintMap);
		
		//利用BufferedImage画出二维码图形结构(matrixWidth 是行二维码像素点)
		int width = bitMatrix.getWidth();
		System.out.println(width);
		BufferedImage bufferedImage = new BufferedImage(width-200,width-200,BufferedImage.TYPE_INT_RGB);
		
		bufferedImage.createGraphics();
		Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, width);
		
		graphics.setColor(Color.black);
		for(int i=0;i<width;i++) {
			for(int j=0;j<width;j++) {
				if(bitMatrix.get(i, j)) {
					graphics.fillRect(i-100, j-100, 1, 1);
				}
			}
		}
		return ImageIO.write(bufferedImage, imageFormat, outputStream);
	}
	
	 /**
	  * 读二维码并输出携带的信息
	*/
	public static String readQrCode(InputStream inputStream) throws IOException{  
		
		//设置二维码纠错级别ＭＡＰ
		Hashtable<DecodeHintType,Object> hintMap = new Hashtable<DecodeHintType,Object>();  
		hintMap.put(DecodeHintType.CHARACTER_SET, "UTF-8");
				
		//从输入流中获取字符串信息
		BufferedImage image = ImageIO.read(inputStream);  
		//将图像转换为二进制位图源
		LuminanceSource source = new BufferedImageLuminanceSource(image);  
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));  
		QRCodeReader reader = new QRCodeReader();  
		Result result = null ;  
		try {
			result = reader.decode(bitmap, hintMap);  
		} catch (ReaderException e) {
			e.printStackTrace();  
		}
		return result.getText();
    }
}
