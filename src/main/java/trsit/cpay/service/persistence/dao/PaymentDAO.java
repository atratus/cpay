/**
 *
 */
package trsit.cpay.service.persistence.dao;

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
        private final String eventType;

        public UserDebtsQueryProvider(final String eventType) {

            this.eventType = eventType;
        }
        @Override
        public JPQLQuery getQuery(final JPQLQuery baseQuery) {
            final JPQLQuery query = baseQuery.from(payment) //
                    .groupBy(payment.user) //

                    .orderBy(payment.debt.sum().asc());
            if(eventType != null) {
                query .where(payment.paymentEvent.eventType.eq(eventType));
            }
            return query;
        }
        @Override
        public long getCount(final JPQLQuery baseQuery) {
            return baseQuery.from(payment).uniqueResult(payment.user.countDistinct());
        }
    }

    public ItemsSet<UserPayment> getUserDebts(final String eventType) {

        return buildSet(Projections.bean(
                UserPayment.class,
                payment.user,
                payment.paymentValue.sum().as("paymentValue"),
                payment.debt.sum().as("debt")),
                new UserDebtsQueryProvider(eventType));
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
