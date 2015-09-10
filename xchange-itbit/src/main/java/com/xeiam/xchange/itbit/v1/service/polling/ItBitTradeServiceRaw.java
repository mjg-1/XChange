package com.xeiam.xchange.itbit.v1.service.polling;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.itbit.v1.dto.trade.ItBitOrder;
import com.xeiam.xchange.itbit.v1.dto.trade.ItBitPlaceOrderRequest;

public class ItBitTradeServiceRaw extends ItBitBasePollingService {

  private final static DecimalFormat AmountFormat;
  private final static DecimalFormat PriceFormat;

  static {
    AmountFormat = new DecimalFormat();
    AmountFormat.setMaximumFractionDigits(4);
    AmountFormat.setGroupingUsed(false);
    AmountFormat.setRoundingMode(RoundingMode.HALF_UP);

    PriceFormat = new DecimalFormat();
    PriceFormat.setMaximumFractionDigits(2);
    PriceFormat.setGroupingUsed(false);
    PriceFormat.setRoundingMode(RoundingMode.HALF_UP);
  }

  /** Wallet ID used for transactions with this instance */
  private final String walletId;

  /**
   * Constructor
   *
   * @param exchange
   */
  public ItBitTradeServiceRaw(Exchange exchange) {

    super(exchange);

    // wallet Id used for this instance.
    walletId = (String) exchange.getExchangeSpecification().getExchangeSpecificParameters().get("walletId");
  }

  public ItBitOrder[] getItBitOpenOrders() throws IOException {

    ItBitOrder[] orders = itBitAuthenticated
        .getOrders(signatureCreator, new Date().getTime(), exchange.getNonceFactory(), "XBTUSD", "1", "1000", "open", walletId);

    return orders;
  }

  /**
   * Retrieves the set of orders with the given status.
   *
   * @param status
   * @return
   * @throws IOException
   */
  public ItBitOrder[] getItBitOrders(String status) throws IOException {

    ItBitOrder[] orders = itBitAuthenticated
        .getOrders(signatureCreator, new Date().getTime(), exchange.getNonceFactory(), "XBTUSD", "1", "1000", status, walletId);

    return orders;
  }

  public ItBitOrder getItBitOrder(String orderId) throws IOException {

    ItBitOrder order = itBitAuthenticated.getOrder(signatureCreator, new Date().getTime(), exchange.getNonceFactory(), walletId, orderId);

    return order;
  }

  public ItBitOrder placeItBitLimitOrder(LimitOrder limitOrder) throws IOException {

    String side = limitOrder.getType().equals(OrderType.BID) ? "buy" : "sell";
    String baseCurrency = limitOrder.getCurrencyPair().baseSymbol;
    String amount = AmountFormat.format(limitOrder.getTradableAmount());
    String price  = PriceFormat.format(limitOrder.getLimitPrice());

    ItBitOrder postOrder = itBitAuthenticated.postOrder(signatureCreator, new Date().getTime(), exchange.getNonceFactory(), walletId,
        new ItBitPlaceOrderRequest(side, "limit", baseCurrency, amount, price, baseCurrency + limitOrder.getCurrencyPair().counterSymbol));

    return postOrder;
  }

  public void cancelItBitOrder(String orderId) throws IOException {

    itBitAuthenticated.cancelOrder(signatureCreator, new Date().getTime(), exchange.getNonceFactory(), walletId, orderId);
  }

  public ItBitOrder[] getItBitTradeHistory(String currency, String pageNum, String pageLen) throws IOException {

    ItBitOrder[] orders = itBitAuthenticated.getOrders(signatureCreator, new Date().getTime(), exchange.getNonceFactory(), currency, pageNum, pageLen, "filled",
        walletId);
    return orders;
  }
}
