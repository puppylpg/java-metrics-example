package example.net;

import java.net.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * A network interface is the point of interconnection between a device and any of its network connections.
 * 一般我们都称为网卡，但未必都是硬件的，比如loopback（只是非硬件，并不是virtual。虚拟出的子网卡才是virtual）
 *
 * A NetworkInterface object contains a name and a set of IP addresses assigned to it.
 * So binding to any of these addresses will guarantee communication through this interface.
 *
 * lo = loopback
 *
 * pichu@Archer ~ $ ip addr
 *
 * 1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1
 * link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
 * inet 127.0.0.1/8 scope host lo
 * valid_lft forever preferred_lft forever
 * inet6 ::1/128 scope host
 * valid_lft forever preferred_lft forever
 *
 * 2: enp2s0: <NO-CARRIER,BROADCAST,MULTICAST,UP> mtu 1500 qdisc pfifo_fast state DOWN group default qlen 1000
 * link/ether f4:8e:38:f2:06:4c brd ff:ff:ff:ff:ff:ff
 *
 * 3: wlp3s0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc mq state UP group default qlen 1000
 * link/ether 3c:f8:62:da:51:15 brd ff:ff:ff:ff:ff:ff
 * inet 10.236.27.47/20 brd 10.236.31.255 scope global dynamic wlp3s0
 * valid_lft 1020250sec preferred_lft 1020250sec
 * inet6 fe80::aed1:3164:8a76:ea13/64 scope link
 * valid_lft forever preferred_lft forever
 *
 * 4: docker_gwbridge: <NO-CARRIER,BROADCAST,MULTICAST,UP> mtu 1500 qdisc noqueue state DOWN group default
 * link/ether 02:42:75:98:2f:70 brd ff:ff:ff:ff:ff:ff
 * inet 172.18.0.1/16 brd 172.18.255.255 scope global docker_gwbridge
 * valid_lft forever preferred_lft forever
 *
 * 5: docker0: <NO-CARRIER,BROADCAST,MULTICAST,UP> mtu 1500 qdisc noqueue state DOWN group default
 * link/ether 02:42:6d:49:b9:be brd ff:ff:ff:ff:ff:ff
 * inet 172.17.0.1/16 brd 172.17.255.255 scope global docker0
 * valid_lft forever preferred_lft forever
 *
 * 6: vboxnet0: <BROADCAST,MULTICAST> mtu 1500 qdisc noop state DOWN group default qlen 1000
 * link/ether 0a:00:27:00:00:00 brd ff:ff:ff:ff:ff:ff
 *
 * @author liuhaibo on 2019/08/02
 */
public class NetworkInterfaceDemo {

    public static void main(String... args) throws SocketException, UnknownHostException {
        allNetworkInterfaces();
        singleInterface();
        hostName();
        networkInterfaceInfo();
        wlp3s0();
        docker0();
    }

    /**
     * name:docker0 (docker0)
     * name:docker_gwbridge (docker_gwbridge)
     * name:wlp3s0 (wlp3s0)
     * name:lo (lo)
     */
    private static void allNetworkInterfaces() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface networkInterface : Collections.list(networkInterfaces)) {
            System.out.println(networkInterface);
        }
    }

    /**
     * lo: name:lo (lo)
     * 127.0.0.1: name:lo (lo)
     * localhost: name:lo (lo)
     * LocalHost: null
     * LoopbackAddress: name:lo (lo)
     * index 1: name:lo (lo)
     */
    private static void singleInterface() throws SocketException, UnknownHostException {
        // get by name
        System.out.println("lo: " + NetworkInterface.getByName("lo"));

        // get by InetAddress
        System.out.println("127.0.0.1: " + NetworkInterface.getByInetAddress(
                InetAddress.getByAddress(new byte[]{127, 0, 0, 1})
        ));
        System.out.println("localhost: " + NetworkInterface.getByInetAddress(
                InetAddress.getByName("localhost")
        ));
        System.out.println("LocalHost: " + NetworkInterface.getByInetAddress(
                InetAddress.getLocalHost()
        ));
        System.out.println("LoopbackAddress: " + NetworkInterface.getByInetAddress(
                InetAddress.getLoopbackAddress()
        ));

        // get by index: >= Java 7
        System.out.println("index 1: " + NetworkInterface.getByIndex(1));
    }

    private static void networkInterfaceInfo() throws SocketException {
        NetworkInterface networkInterface = NetworkInterface.getByName("lo");
        // lo
        System.out.println(networkInterface.getName());
        // lo
        System.out.println(networkInterface.getDisplayName());
        // null
        System.out.println(byteArrayToHex(networkInterface.getHardwareAddress()));
        // 1
        System.out.println(networkInterface.getIndex());
        // 65536
        System.out.println(networkInterface.getMTU());
        // null
        System.out.println(networkInterface.getParent());
        // false：子网卡都是虚拟网卡
        System.out.println(networkInterface.getSubInterfaces().hasMoreElements());

        // true
        System.out.println(networkInterface.isUp());
        // true
        System.out.println(networkInterface.isLoopback());
        // false
        System.out.println(networkInterface.isPointToPoint());
        // false: 网卡虚拟出来的子网卡是virtual的，getSubInterfaces()
        System.out.println(networkInterface.isVirtual());

        // InetAddress: ips bind to this interface
        Enumeration<InetAddress> ips = networkInterface.getInetAddresses();
        // 0:0:0:0:0:0:0:1%lo
        System.out.println(ips.nextElement().getHostAddress());

        // InterfaceAddress: ips bind to this interface, more powerful than InetAddress, such as broadcast address
        List<InterfaceAddress> powerIps = networkInterface.getInterfaceAddresses();
        // 2
        System.out.println(powerIps.size());
        InterfaceAddress ip0 = powerIps.get(0);
        InterfaceAddress ip1 = powerIps.get(1);
        // /0:0:0:0:0:0:0:1%lo
        System.out.println(ip0.getAddress());
        // null
        System.out.println(ip0.getBroadcast());
        // 128
        System.out.println(ip0.getNetworkPrefixLength());
        // /127.0.0.1
        System.out.println(ip1.getAddress());
        // null
        System.out.println(ip1.getBroadcast());
        // 8
        System.out.println(ip1.getNetworkPrefixLength());
    }

    private static void wlp3s0() throws SocketException {
        NetworkInterface wifi = NetworkInterface.getByName("wlp3s0");
        // 3C-F8-62-DA-51-15
        System.out.println(byteArrayToHex(wifi.getHardwareAddress()));
        // false
        System.out.println(wifi.isLoopback());
        // true
        System.out.println(wifi.supportsMulticast());
        // false
        System.out.println(wifi.isVirtual());
    }

    private static void docker0() throws SocketException {
        NetworkInterface docker0 = NetworkInterface.getByName("docker0");
        // 02-42-6D-49-B9-BE
        System.out.println(byteArrayToHex(docker0.getHardwareAddress()));
        // false
        System.out.println(docker0.isLoopback());
        // true
        System.out.println(docker0.supportsMulticast());
        // false
        System.out.println(docker0.isVirtual());
    }

    /**
     * Archer/127.0.1.1
     */
    private static void hostName() throws UnknownHostException {
        // it gets /etc/hostname, which is Archer for my Debian.
        // then retrieve Archer in /etc/hosts for ip 127.0.1.1, not 127.0.0.1
        // so this is not localhost, but hostname
        // real localhost binds to 127.0.0.1
        System.out.println(InetAddress.getLocalHost());
    }

    private static String byteArrayToHex(byte[] mac) {
        if (mac == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
        return sb.toString();
    }
}
