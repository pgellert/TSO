# Timestamp Ordering (TSO) store

A simplified implementation of a thread-safe key-value store using the TSO algorithm to guarantee a serializable order on transactions.

It works as follows:
 * A transaction can be started by calling startTx on the ObjectStore. This returns a version object that is associated with the transaction from then on.
 * Get and put requests can be made to the ObjectStore using the version object of the transaction. The request fails if another transaction with version greater than our has accessed the object since the start of the transaction. Then, the transaction has to be aborted and rolled back to its initial state.
 * The transaction is committed by calling commitTx on the ObjectStore. This enables the version object to be reused.

The simplification is that the test only has get requests in its transactions, therefore not needing a rollback mechanism on abort.
