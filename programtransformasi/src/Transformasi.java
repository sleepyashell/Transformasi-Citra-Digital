import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Transformasi Geometri: Translasi, Rotasi, Scaling, Flip
 * Menggunakan Java AWT BufferedImage & AffineTransform
 */
public class Transformasi {

    // Muat gambar dari file 
    public static BufferedImage loadImage(String path) throws Exception {
        return ImageIO.read(new File(path));
    }

    // Simpan gambar ke file
    public static void saveImage(BufferedImage img, String path) throws Exception {
        ImageIO.write(img, "png", new File(path));
    }

    // Konversi ke Grayscale
    public static BufferedImage toGrayscale(BufferedImage src) {
        BufferedImage gray = new BufferedImage(
            src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = gray.createGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return gray;
    }

    // 1. TRANSLASI
    //    Menggeser gambar sejauh (tx, ty) piksel
    public static BufferedImage translasi(BufferedImage src, int tx, int ty) {
        int newW = src.getWidth();
        int newH = src.getHeight();
        BufferedImage result = new BufferedImage(newW, newH, src.getType());
        Graphics2D g = result.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, newW, newH);
        // Gunakan AffineTransform untuk translasi
        AffineTransform at = AffineTransform.getTranslateInstance(tx, ty);
        g.drawImage(src, at, null);
        g.dispose();
        return result;
    }

    // 2. ROTASI
    //    Memutar gambar sebesar 'angle' derajat terhadap titik pusat
    public static BufferedImage rotasi(BufferedImage src, double angleDeg) {
        double rad = Math.toRadians(angleDeg);
        int W = src.getWidth(), H = src.getHeight();
        // Hitung ukuran canvas baru agar gambar tidak terpotong
        double cos = Math.abs(Math.cos(rad)), sin = Math.abs(Math.sin(rad));
        int newW = (int)(W * cos + H * sin);
        int newH = (int)(W * sin + H * cos);
        BufferedImage result = new BufferedImage(newW, newH, src.getType());
        Graphics2D g = result.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, newW, newH);
        // Putar di sekitar titik tengah canvas baru
        AffineTransform at = new AffineTransform();
        at.translate(newW / 2.0, newH / 2.0);
        at.rotate(rad);
        at.translate(-W / 2.0, -H / 2.0);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                           RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(src, at, null);
        g.dispose();
        return result;
    }

    // 3. SCALING
    //    Mengubah ukuran gambar dengan faktor sx (x) dan sy (y)
    public static BufferedImage scaling(BufferedImage src,
                                        double sx, double sy) {
        int newW = (int)(src.getWidth()  * sx);
        int newH = (int)(src.getHeight() * sy);
        BufferedImage result = new BufferedImage(newW, newH, src.getType());
        Graphics2D g = result.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                           RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // AffineTransform scale
        AffineTransform at = AffineTransform.getScaleInstance(sx, sy);
        g.drawImage(src, at, null);
        g.dispose();
        return result;
    }

    // 4. FLIP HORIZONTAL
    public static BufferedImage flipHorizontal(BufferedImage src) {
        int W = src.getWidth(), H = src.getHeight();
        BufferedImage result = new BufferedImage(W, H, src.getType());
        Graphics2D g = result.createGraphics();
        // Scale x=-1 lalu translate agar tidak keluar frame
        AffineTransform at = AffineTransform.getScaleInstance(-1.0, 1.0);
        at.translate(-W, 0);
        g.drawImage(src, at, null);
        g.dispose();
        return result;
    }

    // 5. FLIP VERTIKAL
    public static BufferedImage flipVertikal(BufferedImage src) {
        int W = src.getWidth(), H = src.getHeight();
        BufferedImage result = new BufferedImage(W, H, src.getType());
        Graphics2D g = result.createGraphics();
        // Scale y=-1 lalu translate
        AffineTransform at = AffineTransform.getScaleInstance(1.0, -1.0);
        at.translate(0, -H);
        g.drawImage(src, at, null);
        g.dispose();
        return result;
    }

    // MAIN
    public static void main(String[] args) throws Exception {
        String inputPath = "minji.jpg";
        if (args.length > 0) inputPath = args[0];

        System.out.println("=== Transformasi Geometri ===");
        System.out.println("Input: " + inputPath);

        // Load gambar RGB
        BufferedImage imgRGB = loadImage(inputPath);
        // Buat versi grayscale
        BufferedImage imgGray = toGrayscale(imgRGB);
        saveImage(imgGray, "original_gray.png");

        // RGB
        saveImage(translasi(imgRGB, 50, 40),      "translasi_rgb.png");
        System.out.println("[RGB] Translasi    -> translasi_rgb.png");

        saveImage(rotasi(imgRGB, 45),              "rotasi_rgb.png");
        System.out.println("[RGB] Rotasi 45deg -> rotasi_rgb.png");

        saveImage(scaling(imgRGB, 0.5, 0.5),       "scaling_rgb.png");
        System.out.println("[RGB] Scaling 0.5x -> scaling_rgb.png");

        saveImage(flipHorizontal(imgRGB),          "flip_horizontal_rgb.png");
        System.out.println("[RGB] Flip H       -> flip_horizontal_rgb.png");

        saveImage(flipVertikal(imgRGB),            "flip_vertical_rgb.png");
        System.out.println("[RGB] Flip V       -> flip_vertical_rgb.png");

        // Grayscale
        saveImage(translasi(imgGray, 50, 40),      "translasi_gray.png");
        System.out.println("[Gray] Translasi    -> translasi_gray.png");

        saveImage(rotasi(imgGray, 45),             "rotasi_gray.png");
        System.out.println("[Gray] Rotasi 45deg -> rotasi_gray.png");

        saveImage(scaling(imgGray, 0.5, 0.5),      "scaling_gray.png");
        System.out.println("[Gray] Scaling 0.5x -> scaling_gray.png");

        saveImage(flipHorizontal(imgGray),         "flip_horizontal_gray.png");
        System.out.println("[Gray] Flip H       -> flip_horizontal_gray.png");

        saveImage(flipVertikal(imgGray),           "flip_vertical_gray.png");
        System.out.println("[Gray] Flip V       -> flip_vertical_gray.png");

        System.out.println("\nSemua transformasi selesai!");
    }
}