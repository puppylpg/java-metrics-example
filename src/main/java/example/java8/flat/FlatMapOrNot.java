package example.java8.flat;

import java.util.Optional;

/**
 * @author liuhaibo on 2018/03/11
 */
public class FlatMapOrNot {

    public static void main(String... args) {
        Optional<Computer> computer = Optional.empty();

        // public<U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper)
        // flatMap接受一个`t -> Optional<u>`的函数，正如computer的flatMap接受一个返回Optional<SoundCard>的函数f，
        // 其return Objects.requireNonNull(mapper.apply(value));其实就是直接return了f的值，没再包一层Optional，
        // 但此时返回值已经是Optional<SoundCard>了，否则再包一层的话，就是Optional<Optional<SoundCard>>了。
        // 所以flatMap的作用，就是最终还是只返回包裹着一层Optional的东西。

        // public<U> Optional<U> map(Function<? super T, ? extends U> mapper)
        // map就是只把Optional里的t变成u，
        // 其return Optional.ofNullable(mapper.apply(value));

        //所以总而言之，map和flatMap都只返回一层Optioanl（或Stream），前者接受`t -> u`，或者接受`t -> Optional<U> u`
        String version = computer
                .flatMap(Computer::getSoundCard)
                .flatMap(SoundCard::getUsb)
                .map(Usb::getVersion)
                .orElse("Unknown");

        System.out.println(version);
    }

    private static class Computer {
        private Optional<SoundCard> soundCard;

        Computer(Optional<SoundCard> soundCard) {
            this.soundCard = soundCard;
        }

        Optional<SoundCard> getSoundCard() {
            return soundCard;
        }
    }

    private static class SoundCard {
        private Optional<Usb> usb;

        SoundCard(Optional<Usb> usb) {
            this.usb = usb;
        }

        Optional<Usb> getUsb() {
            return usb;
        }
    }

    private static class Usb {
        String version;

        Usb(String v) {
            version = v;
        }

        String getVersion() {
            return version;
        }
    }
}
