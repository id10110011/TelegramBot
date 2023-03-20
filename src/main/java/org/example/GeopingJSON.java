package org.example;

import java.util.ArrayList;

public class GeopingJSON {
    private String ip;
    private boolean is_alive;
    private double min_rtt;
    private double avg_rtt;
    private double max_rtt;
    private ArrayList<Double> rtts;
    private int packets_sent;
    private int packets_received;
    private double packets_loss;
    private Location from_loc;

    public String getIp() {
        return ip;
    }

    public boolean isIs_alive() {
        return is_alive;
    }

    public double getMin_rtt() {
        return min_rtt;
    }

    public double getAvg_rtt() {
        return avg_rtt;
    }

    public double getMax_rtt() {
        return max_rtt;
    }

    public ArrayList<Double> getRtts() {
        return rtts;
    }

    public int getPackets_sent() {
        return packets_sent;
    }

    public int getPackets_received() {
        return packets_received;
    }

    public double getPackets_loss() {
        return packets_loss;
    }

    public Location getFrom_loc() {
        return from_loc;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setIs_alive(boolean is_alive) {
        this.is_alive = is_alive;
    }

    public void setMin_rtt(double min_rtt) {
        this.min_rtt = min_rtt;
    }

    public void setAvg_rtt(double avg_rtt) {
        this.avg_rtt = avg_rtt;
    }

    public void setMax_rtt(double max_rtt) {
        this.max_rtt = max_rtt;
    }

    public void setRtts(ArrayList<Double> rtts) {
        this.rtts = rtts;
    }

    public void setPackets_sent(int packets_sent) {
        this.packets_sent = packets_sent;
    }

    public void setPackets_received(int packets_received) {
        this.packets_received = packets_received;
    }

    public void setPackets_loss(double packets_loss) {
        this.packets_loss = packets_loss;
    }

    public void setFrom_loc(Location from_loc) {
        this.from_loc = from_loc;
    }

    @Override
    public String toString() {
        return "GeopingJSON{" +
                "ip='" + ip + '\'' +
                ", is_alive=" + is_alive +
                ", min_rtt=" + min_rtt +
                ", avg_rtt=" + avg_rtt +
                ", max_rtt=" + max_rtt +
                ", rtts=" + rtts +
                ", packets_sent=" + packets_sent +
                ", packets_received=" + packets_received +
                ", packets_loss=" + packets_loss +
                ", from_loc=" + from_loc +
                '}';
    }
}
