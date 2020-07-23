package com.unindra.flq;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Constants to images names for levels.
 * @author dawidsvk
 *
 */
public class Resources {

    public static final int e01x01 = 1;
    public static final int e01x02 = 2;
    public static final int e01x03 = 3;
    public static final int e01x04 = 4;
    public static final int e01x05 = 5;
    public static final int e01x06 = 6;
    public static final int e01x07 = 7;
    public static final int e01x08 = 8;
    public static final int e01x09 = 9;
    public static final int e01x10 = 10;
    public static final int e01x11 = 11;
    public static final int e01x12 = 12;
    public static final int e01x13 = 13;
    public static final int e01x14 = 14;
    public static final int e01x15 = 15;
    
    public static final int e02x01 = 16;
    public static final int e02x02 = 17;
    public static final int e02x03 = 18;
    public static final int e02x04 = 19;
    public static final int e02x05 = 20;
    public static final int e02x06 = 21;
    public static final int e02x07 = 22;
    public static final int e02x08 = 23;
    public static final int e02x09 = 24;
    public static final int e02x10 = 25;
    public static final int e02x11 = 26;
    public static final int e02x12 = 27;
    public static final int e02x13 = 28;
    public static final int e02x14 = 29;
    public static final int e02x15 = 30;
    
    public static final int e03x01 = 31;
    public static final int e03x02 = 32;
    public static final int e03x03 = 33;
    public static final int e03x04 = 34;
    public static final int e03x05 = 35;
    public static final int e03x06 = 36;
    public static final int e03x07 = 37;
    public static final int e03x08 = 38;
    public static final int e03x09 = 39;
    public static final int e03x10 = 40;
    public static final int e03x11 = 41;
    public static final int e03x12 = 42;
    public static final int e03x13 = 43;
    public static final int e03x14 = 44;
    public static final int e03x15 = 45;
    
    public static final int e04x01 = 46;
    public static final int e04x02 = 47;
    public static final int e04x03 = 48;
    public static final int e04x04 = 49;
    public static final int e04x05 = 50;
    public static final int e04x06 = 51;
    public static final int e04x07 = 52;
    public static final int e04x08 = 53;
    public static final int e04x09 = 54;
    public static final int e04x10 = 55;
    public static final int e04x11 = 56;
    public static final int e04x12 = 57;
    public static final int e04x13 = 58;
    public static final int e04x14 = 59;
    public static final int e04x15 = 60;
    
    public static final int e05x01 = 61;
    public static final int e05x02 = 62;
    public static final int e05x03 = 63;
    public static final int e05x04 = 64;
    public static final int e05x05 = 65;
    public static final int e05x06 = 66;
    public static final int e05x07 = 67;
    public static final int e05x08 = 68;
    public static final int e05x09 = 69;
    public static final int e05x10 = 70;
    public static final int e05x11 = 71;
    public static final int e05x12 = 72;
    public static final int e05x13 = 73;
    public static final int e05x14 = 74;
    public static final int e05x15 = 75;
    
    public static final int e06x01 = 76;
    public static final int e06x02 = 77;
    public static final int e06x03 = 78;
    public static final int e06x04 = 79;
    public static final int e06x05 = 80;
    public static final int e06x06 = 81;
    public static final int e06x07 = 82;
    public static final int e06x08 = 83;
    public static final int e06x09 = 84;
    public static final int e06x10 = 85;
    public static final int e06x11 = 86;
    public static final int e06x12 = 87;
    public static final int e06x13 = 88;
    public static final int e06x14 = 89;
    public static final int e06x15 = 90;
    
    public static final int e07x01 = 91;
    public static final int e07x02 = 92;
    public static final int e07x03 = 93;
    public static final int e07x04 = 94;
    public static final int e07x05 = 95;
    public static final int e07x06 = 96;
    public static final int e07x07 = 97;
    public static final int e07x08 = 98;
    public static final int e07x09 = 99;
    public static final int e07x10 = 100;
    public static final int e07x11 = 101;
    public static final int e07x12 = 102;
    public static final int e07x13 = 103;
    public static final int e07x14 = 104;
    public static final int e07x15 = 105;
    	    
	public static final int e08x01 = 106;
    public static final int e08x02 = 107;
    public static final int e08x03 = 108;
    public static final int e08x04 = 109;
    public static final int e08x05 = 110;
    public static final int e08x06 = 111;
    public static final int e08x07 = 112;
    public static final int e08x08 = 113;
    public static final int e08x09 = 114;
    public static final int e08x10 = 115;
    public static final int e08x11 = 116;
    public static final int e08x12 = 117;
    public static final int e08x13 = 118;
    public static final int e08x14 = 119;
    public static final int e08x15 = 120;
    
	public static final int e09x01 = 121;
    public static final int e09x02 = 122;
    public static final int e09x03 = 123;
    public static final int e09x04 = 124;
    public static final int e09x05 = 125;
    public static final int e09x06 = 126;
    public static final int e09x07 = 127;
    public static final int e09x08 = 128;
    public static final int e09x09 = 129;
    public static final int e09x10 = 130;
    public static final int e09x11 = 131;
    public static final int e09x12 = 132;
    public static final int e09x13 = 133;
    public static final int e09x14 = 134;
    public static final int e09x15 = 135;
    	    
	public static final int e10x01 = 136;
    public static final int e10x02 = 137;
    public static final int e10x03 = 138;
    public static final int e10x04 = 139;
    public static final int e10x05 = 140;
    public static final int e10x06 = 141;
    public static final int e10x07 = 142;
    public static final int e10x08 = 143;
    public static final int e10x09 = 144;
    public static final int e10x10 = 145;
    public static final int e10x11 = 146;
    public static final int e10x12 = 147;
    public static final int e10x13 = 148;
    public static final int e10x14 = 149;
    public static final int e10x15 = 150;
    	    	    
	public static final int e11x01 = 151;
    public static final int e11x02 = 152;
    public static final int e11x03 = 153;
    public static final int e11x04 = 154;
    public static final int e11x05 = 155;
    public static final int e11x06 = 156;
    public static final int e11x07 = 157;
    public static final int e11x08 = 158;
    public static final int e11x09 = 159;
    public static final int e11x10 = 160;
    public static final int e11x11 = 161;
    public static final int e11x12 = 162;
    public static final int e11x13 = 163;
    public static final int e11x14 = 164;
    public static final int e11x15 = 165;
    	    	    	    
	public static final int e12x01 = 166;
    public static final int e12x02 = 167;
    public static final int e12x03 = 168;
    public static final int e12x04 = 169;
    public static final int e12x05 = 170;
    public static final int e12x06 = 171;
    public static final int e12x07 = 172;
    public static final int e12x08 = 173;
    public static final int e12x09 = 174;
    public static final int e12x10 = 175;
    public static final int e12x11 = 176;
    public static final int e12x12 = 177;
    public static final int e12x13 = 178;
    public static final int e12x14 = 179;
    public static final int e12x15 = 180;
    	    	    	    	    
	public static final int e13x01 = 181;
    public static final int e13x02 = 182;
    public static final int e13x03 = 183;
    public static final int e13x04 = 184;
    public static final int e13x05 = 185;
    public static final int e13x06 = 186;
    public static final int e13x07 = 187;
    public static final int e13x08 = 188;
    public static final int e13x09 = 189;
    public static final int e13x10 = 190;
    public static final int e13x11 = 191;
    public static final int e13x12 = 192;
    public static final int e13x13 = 193;
    public static final int e13x14 = 194;
    public static final int e13x15 = 195;
    	    	    	    	    	    
	public static final int e14x01 = 196;
    public static final int e14x02 = 197;
    public static final int e14x03 = 198;
    public static final int e14x04 = 199;
    public static final int e14x05 = 200;
    public static final int e14x06 = 201;
    public static final int e14x07 = 202;
    public static final int e14x08 = 203; 
    public static final int e14x09 = 204;
    public static final int e14x10 = 205;
    public static final int e14x11 = 206;
    public static final int e14x12 = 207;
    public static final int e14x13 = 208;
    public static final int e14x14 = 209;
    public static final int e14x15 = 210;
    
    public static final int TOTAL_LEVELS = 210;
    
    
    public static Bitmap loadScaledBitmap(String pathName, int reqWidth, int reqHeight){
	
	InputStream is = null;
	AssetManager am = X.context.getAssets();
	
	try {
	    is = am.open(pathName);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
	final BitmapFactory.Options options = new BitmapFactory.Options();
	options.inJustDecodeBounds = true;
	Bitmap decodeStream = BitmapFactory.decodeStream(is);
	options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	options.inJustDecodeBounds = false;
	return BitmapFactory.decodeStream(is, null, options);
	
    }
    
    public static Bitmap loadBitmap(String pathName){
	
	try {
	    InputStream is;
	    AssetManager am = X.context.getAssets();
	    is = am.open(pathName);
	    return  BitmapFactory.decodeStream(is);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }
    
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

    
}
