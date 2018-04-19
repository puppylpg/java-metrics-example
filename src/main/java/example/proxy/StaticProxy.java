package example.proxy;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 和程序猿有相同的接口，外部给需求的时候，给这货就行了，
 * 但实际上，这货是调用其他程序猿做的，这是个假程序员（是谓代理）。
 *
 * @author liuhaibo on 2018/04/18
 */
@NoArgsConstructor
@AllArgsConstructor
public class StaticProxy implements ICoder {

    @Setter
    private ICoder coder;

    @Override
    public void implementDemands(String demand) {
        // 代理要挡一些不合理需求，给程序猿最纯粹的任务
        if ("illegal".equals(demand)) {
            System.out.println("No! ILLEGAL demand!");
            return;
        }
        System.out.println("OK, I'll find a coder to do that.");
        coder.implementDemands(demand);
    }

    @Override
    public void estimateTime(String demand) {
        coder.estimateTime(demand);
    }

}
