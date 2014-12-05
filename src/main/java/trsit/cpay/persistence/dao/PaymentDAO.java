/**
 *
 */
package trsit.cpay.persistence.dao;

import static trsit.cpay.persistence.model.QPayment.payment;

import org.springframework.stereotype.Repository;

import trsit.cpay.data.ItemsSet;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.Projections;

/**
 * @author black
 *
 */
@Repository
public class PaymentDAO extends AbstractDAO {


    private static class UserDebtsQueryProvider implements QueryProvider {
        private static final long serialVersionUID = 1L;
        @Override
        public JPQLQuery getQuery(final JPQLQuery query) {
            return query.from(payment) //
                    .groupBy(payment.user) //
                    .orderBy(payment.paymentValue.sum()/*new NumberPath<BigDecimal>(BigDecimal.class, "debt")*/.asc());
        }
    }

    public ItemsSet<UserDebt> getUserDebts() {

        return buildSet(Projections.bean(
                UserDebt.class, payment.user, payment.paymentValue.sum().as("debt")),
                new UserDebtsQueryProvider());
    }
}
