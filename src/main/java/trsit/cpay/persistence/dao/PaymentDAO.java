/**
 *
 */
package trsit.cpay.persistence.dao;

import static trsit.cpay.persistence.model.QPayment.payment;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import trsit.cpay.data.ItemsSet;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.Projections;
/**
 * @author black
 */
@Repository
public class PaymentDAO extends AbstractDAO {

    @Inject
    private JPQLQueryFactory queryFactory;

    private static class UserDebtsQueryProvider implements QueryProvider {
        private static final long serialVersionUID = 1L;
        @Override
        public JPQLQuery getQuery(final JPQLQuery query) {
            return query.from(payment) //
                    .groupBy(payment.user) //
                    .orderBy(payment.debt.sum().asc());
        }
        @Override
        public long getCount(final JPQLQuery baseQuery) {
            return baseQuery.from(payment).uniqueResult(payment.user.countDistinct());
        }
    }

    public ItemsSet<UserPayment> getUserDebts() {

        return buildSet(Projections.bean(
                UserPayment.class,
                payment.user,
                payment.paymentValue.sum().as("paymentValue"),
                payment.debt.sum().as("debt")),
                new UserDebtsQueryProvider());
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotal() {
        final BigDecimal total = queryFactory.createBaseQuery().from(payment).uniqueResult(payment.paymentValue.sum());
        return total;
    }

    @Transactional(readOnly = true)
    public long participantsCount() {
        return queryFactory.createBaseQuery().from(payment).uniqueResult(payment.user.count());
    }
}
