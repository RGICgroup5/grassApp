package nl.faunafonds.grassdamageassessment.other;

public class ConvertCoordinates {
	public static double[] RDtoLatLon(double xCoordRD, double yCoordRD) {

		double d1 = xCoordRD - 155000;
		double d2 = yCoordRD - 463000;
		double r = Math.sqrt((d1 * d1) + (d2 * d2));
		double sa = d1 / r;
		double ca = d2 / r;
		double psi = Atan2(0.9999079 * 2 * 6382644.571, r) * 2;
		double cpsi = Math.cos(psi);
		double spsi = Math.sin(psi);
		double sb = ca * Math.cos(0.909684756747208) * spsi
				+ Math.sin(0.909684756747208) * cpsi;
		double cb = Math.sqrt(1 - (sb * sb));
		double b = Math.acos(cb);
		double sdl = sa * spsi / cb;
		double dl = Math.atan(sdl / Math.sqrt(-sdl * sdl + 1));
		double lambda = dl / 1.00047585668 + 9.40320375215393E-02;
		double w = (Math.log(Math.tan(b / 2 + 3.14159265358979 / 4)))
				/ Math.log(2.718281828459);
		double q = (w - 0.003773953832) / 1.00047585668;
		double phiprime = Math.atan(Math.exp(q)) * 2 - 3.14159265358979 / 2;
		double dq1 = 0.08169683122 / 2 * (Math.log((0.08169683122 * Math
				.sin(phiprime) + 1) / (1 - 0.08169683122 * Math.sin(phiprime))) / Math
				.log(2.718281828459));
		double phi1 = Math.atan(Math.exp(q + dq1)) * 2 - 3.14159265358979 / 2;
		double dq2 = 0.08169683122 / 2 * (Math.log((0.08169683122 * Math
				.sin(phi1) + 1) / (1 - 0.08169683122 * Math.sin(phi1))) / Math
				.log(2.718281828459));
		double phi2 = Math.atan(Math.exp(q + dq2)) * 2 - 3.14159265358979 / 2;
		double dq3 = 0.08169683122 / 2 * (Math.log((0.08169683122 * Math
				.sin(phi2) + 1) / (1 - 0.08169683122 * Math.sin(phi2))) / Math
				.log(2.718281828459));
		double phi3 = Math.atan(Math.exp(q + dq3)) * 2 - 3.14159265358979 / 2;
		double dq4 = 0.08169683122 / 2 * (Math.log((0.08169683122 * Math
				.sin(phi3) + 1) / (1 - 0.08169683122 * Math.sin(phi3))) / Math
				.log(2.718281828459));
		double phi4 = Math.atan(Math.exp(q + dq4)) * 2 - 3.14159265358979 / 2;
		double lambd = lambda / 3.14159265358979 * 180;
		double phi5 = phi4 / 3.14159265358979 * 180;

		double dphi = phi5 - 52;
		double dlam = lambd - 5;
		double phicor = (-96.862 - dphi * 11.714 - dlam * 0.125) * 0.00001;
		double lamcor = (dphi * 0.329 - 37.902 - dlam * 14.667) * 0.00001;
		double phiwgs = phi5 + phicor;
		double lamwgs = lambd + lamcor;

		double[] coordinates = new double[2];
		coordinates[0] = phiwgs;
		coordinates[1] = lamwgs;

		return coordinates;

	}
	
	private static double Atan2(double X, double Y) {
		double atan2;
		if (X == 0 && Y == 0) {
			atan2 = 0;
		} else if (X == 0)
			X = 0.00000000001;

		atan2 = 2 * Math.atan(Y / (Math.sqrt((X * X) + (Y * Y)) + X));
		return atan2;
	}
}
