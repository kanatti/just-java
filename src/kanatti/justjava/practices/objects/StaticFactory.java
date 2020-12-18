package kanatti.justjava.practices.objects;

import kanatti.justjava.practices.objects.pickup.Pickup;
import kanatti.justjava.practices.objects.pickup.PickupTruck;


public class StaticFactory {

    public static void main(String[] args) {
        Subscription sub1 = Subscription.monthlySubscription();
        Subscription sub2 = Subscription.yearlySubscription();
        System.out.println(sub1.getSubscriptionType());
        System.out.println(sub2.getSubscriptionType());

        // Returns PickupTruck interface. Client need not know about which class is used
        PickupTruck pickup1 = Pickup.getPickupTruck(200);
        PickupTruck pickup2 = Pickup.getPickupTruck(100);

        pickup1.deliver();
        pickup2.deliver();
        System.out.println(pickup1.getMessage());
        System.out.println(pickup2.getMessage());

    }
}
