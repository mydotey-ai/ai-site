package org.mydotey.ai.site.media.infrastructure.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.mydotey.ai.site.media.infrastructure.config.ImageProcessProperties;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片处理服务
 *
 * @author AI-Site
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImageProcessService {

    private final ImageProcessProperties properties;

    /**
     * 处理图片：压缩、加水印
     *
     * @param inputStream 原图输入流
     * @param format      图片格式 (jpg, png, etc.)
     * @return 处理后的图片数据
     */
    public ProcessedImage process(InputStream inputStream, String format) throws IOException {
        BufferedImage original = ImageIO.read(inputStream);
        if (original == null) {
            throw new IOException("无法读取图片");
        }

        BufferedImage processed = original;

        // 压缩
        if (properties.getCompress().isEnabled()) {
            processed = compress(processed, format, properties.getCompress().getQuality());
            log.debug("图片压缩完成");
        }

        // 加水印
        if (properties.getWatermark().isEnabled() && !properties.getWatermark().getText().isEmpty()) {
            processed = addWatermark(processed, properties.getWatermark().getText(),
                    properties.getWatermark().getPosition(),
                    properties.getWatermark().getOpacity());
            log.debug("水印添加完成");
        }

        // 生成缩略图
        BufferedImage thumbnail = null;
        if (properties.getThumbnail().isEnabled()) {
            thumbnail = createThumbnail(processed, properties.getThumbnail().getWidth());
            log.debug("缩略图生成完成");
        }

        return new ProcessedImage(processed, thumbnail, original.getWidth(), original.getHeight());
    }

    /**
     * 压缩图片
     */
    private BufferedImage compress(BufferedImage image, String format, float quality) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(image)
                .scale(1.0)
                .outputQuality(quality)
                .outputFormat(format)
                .toOutputStream(os);

        return ImageIO.read(new ByteArrayInputStream(os.toByteArray()));
    }

    /**
     * 创建缩略图
     */
    private BufferedImage createThumbnail(BufferedImage image, int width) throws IOException {
        int height = (int) ((double) image.getHeight() / image.getWidth() * width);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(image)
                .size(width, height)
                .toOutputStream(os);

        return ImageIO.read(new ByteArrayInputStream(os.toByteArray()));
    }

    /**
     * 添加水印
     */
    private BufferedImage addWatermark(BufferedImage image, String text, String position, float opacity) {
        Graphics2D g2d = image.createGraphics();

        // 设置水印样式
        g2d.setColor(new Color(255, 255, 255, (int) (opacity * 255)));
        g2d.setFont(new Font("微软雅黑", Font.BOLD, 24));

        // 计算位置
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        int textHeight = fontMetrics.getHeight();

        int x, y;
        int padding = 20;
        switch (position.toLowerCase()) {
            case "top-left":
                x = padding;
                y = textHeight + padding;
                break;
            case "top-right":
                x = image.getWidth() - textWidth - padding;
                y = textHeight + padding;
                break;
            case "bottom-left":
                x = padding;
                y = image.getHeight() - padding;
                break;
            case "center":
                x = (image.getWidth() - textWidth) / 2;
                y = (image.getHeight() + textHeight) / 2;
                break;
            case "bottom-right":
            default:
                x = image.getWidth() - textWidth - padding;
                y = image.getHeight() - padding;
                break;
        }

        g2d.drawString(text, x, y);
        g2d.dispose();

        return image;
    }

    /**
     * 获取图片信息
     */
    public ImageInfo getImageInfo(InputStream inputStream) throws IOException {
        BufferedImage image = ImageIO.read(inputStream);
        if (image == null) {
            throw new IOException("无法读取图片");
        }
        return new ImageInfo(image.getWidth(), image.getHeight());
    }

    /**
     * 处理后的图片
     */
    public record ProcessedImage(BufferedImage image, BufferedImage thumbnail, int width, int height) {
    }

    /**
     * 图片信息
     */
    public record ImageInfo(int width, int height) {
    }
}
