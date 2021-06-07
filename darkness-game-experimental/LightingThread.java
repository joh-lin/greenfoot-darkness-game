import greenfoot.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.awt.RadialGradientPaint;
import java.awt.Graphics2D;


public class LightingThread implements Runnable
{
    private Thread t;
    private LightSource ls;
    private Camera cam;
    public int[] xPoints;
    public int[] yPoints;

    public LightingThread(LightSource lightSource, Camera cam)
    {
        this.cam = cam;
        this.ls = lightSource;
    }

    public void run()
    {
        float lsrot = ls.getRotation();
        while (lsrot < -180) lsrot += 360;
        while (lsrot > 180) lsrot -= 360;

        List<Point> points = new ArrayList<>();
        for (int i = 0; i < cam.geometry.size(); i++) {
            points.add(cam.geometry.get(i).a);
            points.add(cam.geometry.get(i).b);
        }

        List<Float> angles = pointsToAngles(points, ls);
        List<Float> filteredAngles = new ArrayList<>();
        if (ls.angle != 361) {
            for (int i = 0; i < angles.size(); i++) {
                double a = Math.toDegrees(angles.get(i)) - lsrot;
                while (a < -180) a += 360;
                while (a > 180) a -= 360;
                if (-ls.angle/2 < a && a < ls.angle/2) {
                    filteredAngles.add(angles.get(i));
                }
            }
            filteredAngles.add((float)Math.toRadians(lsrot + ls.angle/2));
            filteredAngles.add((float)Math.toRadians(lsrot - ls.angle/2));
        } else {
            filteredAngles = angles;
        }

        List<Point> raycastHits = castAllRays(filteredAngles, cam.geometry, ls);
        if (ls.angle != 361) raycastHits.add(new Point(ls.getScreenX(cam)-(float)Math.cos(Math.toRadians(lsrot)), ls.getScreenY(cam)-(float)Math.sin(Math.toRadians(lsrot))));
        double[] angles2 = new double[raycastHits.size()];
        xPoints = new int[raycastHits.size()];
        yPoints = new int[raycastHits.size()];

        for (int j = 0; j < raycastHits.size(); j++) {
            double angle = Math.toDegrees(Math.atan2(raycastHits.get(j).y - ls.getScreenY(cam), raycastHits.get(j).x - ls.getScreenX(cam)));
            int i = j - 1;
            while ((i > -1) && (angles2[i] > angle)) {
                angles2[i+1] = angles2[i];
                xPoints[i+1] = xPoints[i];
                yPoints[i+1] = yPoints[i];
                i--;
            }
            angles2[i+1] = angle;
            xPoints[i+1] = (int)Math.round(raycastHits.get(j).x);
            yPoints[i+1] = (int)Math.round(raycastHits.get(j).y);
        }
        
        if (cam.debugMode) drawDebug(xPoints, yPoints, ls);
    }

    // https://github.com/yoyoberenguer/LightEffect/blob/master/Shadows.py
    private List<Point> castAllRays(List<Float> angles, List<Line> geometry, LightSource ls)
    {
        List<Point> raycastHits = new ArrayList<>();
        int lsX = ls.getScreenX(cam);
        int lsY = ls.getScreenY(cam);

        for (int i = 0; i < angles.size(); i++) {
            float angle = angles.get(i);
            float ray_px = lsX;
            float ray_py = lsY;
            double ray_dx = (float)Math.cos(angle);
            double ray_dy = (float)Math.sin(angle);

            float closestHitDist = 99999;
            float closestHitX = 0;
            float closestHitY = 0;

            for (int j = 0; j < geometry.size(); j++) {
                float seg_px = geometry.get(j).a.x;
                float seg_py = geometry.get(j).a.y;
                float seg_dx = geometry.get(j).b.x - seg_px;
                float seg_dy = geometry.get(j).b.y - seg_py;

                // are ray and segment parallel?
                float ray_mag = (float)(Math.pow(ray_dx, 2) + Math.pow(ray_dy, 2));
                float seg_mag = (float)(Math.pow(seg_dx, 2) + Math.pow(seg_dy, 2));

                if ((ray_dx / ray_mag) == (seg_dx / seg_mag) && (ray_dy / ray_mag) == (seg_dy / seg_mag)) {
                    continue;
                }
                
                double t2;
                try {
                    t2 = (ray_dx * (seg_py - ray_py) + ray_dy * (ray_px - seg_px)) / (seg_dx * ray_dy - seg_dy * ray_dx);
                } catch (ArithmeticException e) {
                    t2 = (ray_dx * (seg_py - ray_py) + ray_dy * (ray_px - seg_px)) / (seg_dx * ray_dy - seg_dy * ray_dx - 0.01f);
                }
                
                double t1;
                try {
                    t1 = (seg_px + seg_dx * t2 - ray_px) / ray_dx;
                } catch (ArithmeticException e) {
                    t1 = (seg_px + seg_dx * t2 - ray_px) / (ray_dx - 0.01f);
                }

                if (t1 < 0.001) {
                    continue;
                }

                if (t2 < 0 || t2 > 1) {
                    continue;
                }

                float hit_x = (float)(ray_px + ray_dx * t1);
                float hit_y = (float)(ray_py + ray_dy * t1);
                float dist = (float)t1;

                if (dist < closestHitDist) {
                    closestHitDist = dist;
                    closestHitX = hit_x;
                    closestHitY = hit_y;
                }
            }
            
            if (closestHitDist != 99999) {
                raycastHits.add( new Point(closestHitX, closestHitY));
            }
        }
        return raycastHits;
    }

    private void drawDebug(int[] xPoints, int[] yPoints, LightSource ls)
    {
        int debugCircleSize = 10;
        
        GreenfootImage camImg = cam.getImage();
        for (int i = 0; i < xPoints.length; i++) {
            camImg.setColor(Color.BLUE);
            camImg.drawLine(xPoints[i], yPoints[i], ls.getScreenX(cam), ls.getScreenY(cam));
            camImg.fillOval(xPoints[i]-debugCircleSize/2, yPoints[i]-debugCircleSize/2, debugCircleSize, debugCircleSize);
            
            if (i == 0) {
                camImg.drawLine(xPoints[i], yPoints[i], xPoints[xPoints.length-1], yPoints[yPoints.length-1]);
            } else {
                camImg.drawLine(xPoints[i], yPoints[i], xPoints[i-1], yPoints[i-1]);
            }
        }
    }

    private List<Float> pointsToAngles(List<Point> points, LightSource ls)
    {
        List<Float> angles = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            float dx = points.get(i).x - ls.getScreenX(cam);
            float dy = points.get(i).y - ls.getScreenY(cam);
            float a = (float)Math.atan2(dy, dx);
            angles.add(a);
            angles.add(a + 0.001f);
            angles.add(a - 0.001f);
        }
        return angles;
    }
}