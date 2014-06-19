package net.catchpole.web.site.model.cart;

//   Copyright 2014 catchpole.net
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Invoice implements Iterable<Purchase> {
    private List<Purchase> purchases = new ArrayList<Purchase>();
    private Purchase shipping;

    public Invoice() {
    }

    public Iterator<Purchase> iterator() {
        return purchases.iterator();
    }

    public void addPurchase(Purchase purchase) {
        for (Purchase comparePurchase : purchases) {
            if (comparePurchase.mergablePurchase(purchase)) {
                comparePurchase.setQuantity(comparePurchase.getQuantity() + purchase.getQuantity());
                return;
            }
        }

        this.purchases.add(purchase);
    }

    public void removeZeroPurchases() {
        Iterator<Purchase> i = purchases.iterator();
        while (i.hasNext()) {
            Purchase purchase = i.next();
            if (purchase.getQuantity() < 1) {
                i.remove();
            }
        }
    }

    public int getSubTotal() {
        int total = 0;
        for (Purchase purchase : this) {
            total += purchase.getPrice();
        }
        return total;
    }

    public int getGrandTotal() {
        return getSubTotal() + (shipping != null ? shipping.getItem().getPrice() : 0);
    }

    public void calculateShipping() {
        if (purchases.size() == 0) {
            this.shipping = null;
        } else {
            this.shipping = new Purchase(new Item("HOORAY","", "Shipping", purchases.size() * 1000,null));
        }
    }

    public Purchase getShipping() {
        return shipping;
    }

    public int getTotalItems() {
        int total = 0;

        for (Purchase purchase : purchases) {
            total += purchase.getQuantity();
        }

        return total;
    }

    public void setShipping(Purchase shipping) {
        this.shipping = shipping;
    }

    @Override
    public String toString() {
        return "Invoice{ total=" + getSubTotal() +
                " purchases=" + purchases +
                '}';
    }
}
