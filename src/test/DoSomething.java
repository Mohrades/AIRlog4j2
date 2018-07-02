package test;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import connexions.AIRRequest;
import util.AccountDetails;
import util.AccumulatorInformation;
import util.BalanceAndDate;
import util.DedicatedAccount;
import util.OfferInformation;
import util.PamInformation;
import util.PamInformationList;

public class DoSomething {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String msisdn = "22997938994";
		msisdn = "22967075745";
		msisdn = "22967191973";
		msisdn = "22966857792";
		msisdn = "22997379819";
		msisdn = "22966857792";
		msisdn = "22962893693";
		msisdn = "22961437066";
		msisdn = "22961000002";
		msisdn = "22961437066";
		msisdn = "22961437076";
		msisdn = "22961437076";
		msisdn = "22961437086";
		msisdn = "22961437086";
		msisdn = "22962893693";
		msisdn = "22961437076";
		msisdn = "22962893693";
		msisdn = "22967728181";
		msisdn = "22962893693";

		Date expires = new Date();
		expires.setHours(23);
		expires.setMinutes(59);
		expires.setSeconds(59);
		expires.setDate(31);
		expires.setMonth(4);
		expires.setYear(118);

		List<String> hosts = new LinkedList<String>();
		hosts.add("10.10.5.153:10010"); hosts.add("10.10.40.95:10010"); hosts.add("10.10.5.149:10010"); 

		AIRRequest request = new AIRRequest(hosts, 5, 12500, 4500, 7);
		System.out.println(request.testConnection(msisdn, -8));
		
		System.out.println(request.getBalanceAndDate(msisdn, 0).getAccountValue());

		// System.out.println(request.deleteOffer(msisdn, 641, "eBA", false));
		// System.out.println(request.deleteOffer(msisdn, 642, "eBA", false));
		// System.out.println(request.deleteOffer(msisdn, 643, "eBA", false));
		// System.out.println(request.deleteOffer(msisdn, 644, "eBA", false));

		// 25 MO = 7324,2187500000116736
		
		HashSet<BalanceAndDate> balances = new HashSet<BalanceAndDate>();
		balances.add(new DedicatedAccount(0,2000, null));
		// balances.add(new DedicatedAccount(262, -request.getBalanceAndDate(msisdn, 262).getAccountValue()+ 0, null));

		System.out.println(request.updateBalanceAndDate(msisdn, balances, "TEST", "TEST", "ebafrique"));

		BalanceAndDate balance = request.getBalanceAndDate(msisdn, 0);
		System.out.println(balance.getAccountID() + " " + balance.getAccountValue() + "  " + balance.getExpiryDate() + "  " + balance.getServiceFee());
		// balance = request.getBalanceAndDate(msisdn, 242);
		// System.out.println(balance.getAccountID() + " " + balance.getAccountValue() + "  " + balance.getExpiryDate() + "  " + balance.getServiceFee());

		Date now = new Date();
		for(int i=0 ; i < 10; i++) {
			if(i%2 == 0)
			request.setWaitingForResponse(true);
			else request.setWaitingForResponse(true);
			AccountDetails accountDetails = request.getAccountDetails(msisdn);
			if(accountDetails != null) {
				System.out.println(accountDetails.getAccountGroupID());
				System.out.println(accountDetails.getLanguageIDCurrent());
				System.out.println(accountDetails.getServiceClassCurrent());
				System.out.println(accountDetails.getServiceClassOriginal());
				// System.out.println(accountDetails.getCommunityInformationCurrent()[0]);
			}			
		}

		Date fin = new Date();
		System.out.println(fin.getTime() - now.getTime());

		HashSet<AccumulatorInformation> accumulators = request.getAccumulators(msisdn, new int[][] {{1,1},{2,7}});
		for (AccumulatorInformation ac : accumulators) {
			System.out.println("ACC " + ac.getAccumulatorID() + "  " + ac.getAccumulatorValue() + "  " + ac.getAccumulatorStartDate()  + "  " + ac.getAccumulatorEndDate());
		}

		// HashSet<OfferInformation> offers = request.getOffers(msisdn, new int[][] {{2,500}}, false, "10000000", false);
		HashSet<OfferInformation> offers = request.getOffers(msisdn, new int[][] {{2,700}}, false, null, false);
		for (OfferInformation offer : offers) {
			System.out.println("OFF " + offer.getOfferID() + "  " + offer.getOfferType() + "  " + offer.getStartDate()  + "  " + offer.getExpiryDate());
		}
	}

}
