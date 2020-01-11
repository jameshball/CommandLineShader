import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main
{
  public static void main(String args[]) throws IOException {
    File folder = new File("pngs");
    Stream<File> fileStream =
      Arrays.stream(Objects.requireNonNull(folder.listFiles()));

    List<File> fileList =
      fileStream.filter(File::isFile).collect(Collectors.toList());

    fileList.sort(new SortByDarkness());

    for (File file : fileList) {
      System.out.println(file.getName() + ": " + (SortByDarkness.getDarknessPercentage(file) * 100) + "%");
    }
  }
}

class SortByDarkness implements Comparator<File> {
  private static final int WHITE = 0xffffffff;

  @Override
  public int compare(File file1, File file2) {
    return -Float.compare(getDarknessPercentage(file1),
      getDarknessPercentage(file2));
  }

  public static BufferedImage getImage(File file) {
    BufferedImage image = null;
    try {
      image = ImageIO.read(file);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return image;
  }

  public static float getDarknessPercentage(File file) {
    BufferedImage image = getImage(file);

    float numberOfDarkPixels = 0;

    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        int color = image.getRGB(i, j);

        if (color != WHITE) {
          numberOfDarkPixels++;
        }
      }
    }

    return numberOfDarkPixels / (float) (image.getWidth() * image.getHeight());
  }
}