import java.util.*;
import com.amazon.ion.*;
import com.amazon.ion.system.*;
import software.amazon.awssdk.services.qldbsession.QldbSessionClient;
import software.amazon.qldb.*;

/*
The structure of the sample data
There exists 4 tables in the mynt quantum ledger

1. Person
2. DriversLicense
3. VehicleRegistration
4. Vehicle

we can easily query these tables using the api
how this api actually is able to authenticate me is still unknown
I think it's because this computer I setup aws cli on it so it works already
 */

public final class App {
    public static IonSystem ionSys = IonSystemBuilder.standard().build();
    public static QldbDriver qldbDriver;

    public static void main(final String... args) {
        System.out.println("Initializing the driver");
        // init our driver and select the "mynt" ledger which
        // is the database belonging to this application
        qldbDriver = QldbDriver.builder()
                .ledger("mynt")
                .transactionRetryPolicy(RetryPolicy.builder().maxRetries(3).build())
                .sessionClientBuilder(QldbSessionClient.builder())
                .build();

        // now you can perform CURD operations on the database
        // we're querying auto pop data but at some point want
        // to query our own data which will be populated using
        // data streams from other applications
        qldbDriver.execute(txn -> {
            System.out.println("querying the mynt database");
            Result result = txn.execute("SELECT * FROM Vehicle");
            IonStruct vehicle = (IonStruct) result.iterator().next();
            System.out.println("VIN " + vehicle.get("VIN"));
        });
    }

    // TODO impl this function
    public static void findOwner(final TransactionExecutor txn, final String govId) {
    }
}
