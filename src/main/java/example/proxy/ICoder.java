package example.proxy;

/**
 * 其实，结合RPC想想，这个类就是protocol，是server给client提供的接口！
 *
 * @author liuhaibo on 2018/04/18
 */
public interface ICoder {

    /**
     * finish a demand.
     * @param demand demand
     */
    void implementDemands(String demand);

    /**
     * estimate the time of a demand.
     * @param demand demand
     */
    void estimateTime(String demand);
}
