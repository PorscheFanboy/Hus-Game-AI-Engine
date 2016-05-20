package hus;

public class Driver {


	public static void main(String[] args) {
		HusBoardState hus = new HusBoardState();
		HusMove mov = new HusMove(9, 0);
		hus.move(mov);
		HusMove mov1 = new HusMove(21, 1);
		hus.move(mov1);
		HusMove mov2 = new HusMove(19, 0);
		hus.move(mov2);
		for (int i = 0; i < 32; i++) {
			System.out.println(hus.getPits()[0][i]);
		}
		

	}

}

