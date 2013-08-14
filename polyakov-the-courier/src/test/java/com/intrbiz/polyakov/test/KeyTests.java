package com.intrbiz.polyakov.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;

import java.nio.ByteBuffer;

import org.junit.Test;

import com.intrbiz.gerald.polyakov.Parcel;
import com.intrbiz.gerald.polyakov.PolyakovKey;
import com.intrbiz.gerald.polyakov.PolyakovKey.KeyHash;

public class KeyTests
{
    private static final String encoded_sha1_key = "SHA1:0wxyNgWqqr0vGlzjmjmM21lPFMbY2r/DBogUnh6rnVBMDrMXmUie/Z/Pmr1//9wUWHA8iE7630vEHlsxNVHmuXlbrJsRO/mbhMTiZZb30dc09mvQ4XGtZp5coBCbifwE2a1QnAU4ldYtLQnxPi+fSNCRyW2dwCb/m78zyZcNjxwvvrDG0xR2XWN/b2s1YirPW6oQLpSINsvITxVVY3UPDXRHXdUSC71eHgSwEqnuvBubcd9Bi0vriij0xYjlZIWQfMUmUtKEsX9sPEYPCTeRCFt3XKZXQ1a4v3AvRMcPj8v6Pa4acU7+xAq/6Vz4e3JBUlv9DH+fXO525LBihmALPWY1Fw2YWci9ajHc2jAtmhuSVXwa0gvdU2IR2Mfl9c5YNPIhf9Rh/c24n02TUH1PawbC8V3GZBCi6e9HJCNjo+WdVpUMn8vK+Yl+RUsGlEK9X4Mq9XV1fkju28NBwCseO/DWBmNdDSXvoM4RpglVGVb87+Crdpv8ZDeAGL8/dgrXzCZ1lda2nOPQQJ9+jDhGjOdhBmnqBLgpZXUcWHmDvRbgpObd2OVHfXJs5RaerRhwx+gjObjNVHqVv2PguQvFivjmgX8k5VPbAI8cXJHtd/gQepO+n5hWMg3M6zontuo31OnYWyQKuIVlc+6Z5ftDZ77sJhDdZhoBDFckVDe0P2Ns0qi6yBIqkjtb0e4auXSb4x8AzKKGPBRjkfpnjNsSuGtDmxk5HOZQnebZ5WOoc62McgNlIkTNKNkT9JpV9UQAe8G6iut0P9X/Tz2T0rC9Q6UkhZ2hr3JbcBMyK4yT/aijU4r111rAhvvXrMqBqj7BoYi2q0Y4LUeEMEpD5DqROOUzuZSY6IyVETkJml/XoW3WQZMSgr+//kzo2YTheaEoWjx8R7JFM4IaluxtrfdyyxHh5ozbxRB7kueemwG1NrcZqhL11DqHkv7ntwQnpVd0kLDtWcLadAmukEOP6Zip0kA/n/6lHg/kE0dc0F4QSpkD7f74HJjCi9NYpPXatcPzuHGA5HSI3CJ8tNekw4B0hR2agL3FmOzm8MUaMz8xQJ0MMCN27avMDeBm5uKVBUR8J+SLTjWR4SOnudk7rwnLDURnjLEg4R2LyMaf3mCpePN9kGHx0WJ56TaEdDhbbU4+mfXq5l8cMZUYMjYHCZFd1o63Qlxucg33m5s1f7fhpktY1nMuJt3YJ06HbbIhck3uWIRJWgGj7N77mPOUhJ9zlY2t8L1mWkIfAAgNXkdgi0Uas5jmmnSS2IPSVvaOW9ofQTxP3waHu7LIRx6n2IPg45m7lmyQNYqyBCuoZHDDgD/5QOFooPBzASq9F0Ej3ESP4l6Kc4/Sy9ILAHDsjBA2nQ==";

    private static final byte[] sha1_key = { -45, 12, 114, 54, 5, -86, -86, -67, 47, 26, 92, -29, -102, 57, -116, -37, 89, 79, 20, -58, -40, -38, -65, -61, 6, -120, 20, -98, 30, -85, -99, 80, 76, 14, -77, 23, -103, 72, -98, -3, -97, -49, -102, -67, 127, -1, -36, 20, 88, 112, 60, -120, 78, -6, -33, 75, -60, 30, 91, 49, 53, 81, -26, -71, 121, 91, -84, -101, 17, 59, -7, -101, -124, -60, -30, 101, -106, -9, -47, -41, 52, -10, 107, -48, -31, 113, -83, 102, -98, 92, -96, 16, -101, -119, -4, 4, -39, -83, 80, -100, 5, 56, -107, -42, 45, 45, 9, -15, 62, 47, -97, 72, -48, -111, -55, 109, -99, -64, 38, -1, -101, -65, 51, -55, -105, 13, -113, 28, 47, -66, -80, -58, -45, 20, 118, 93, 99, 127, 111, 107, 53, 98, 42, -49, 91, -86, 16, 46, -108, -120, 54, -53, -56, 79, 21, 85, 99, 117, 15, 13, 116, 71, 93, -43, 18, 11, -67, 94, 30, 4, -80, 18, -87, -18, -68, 27, -101, 113, -33, 65, -117, 75, -21, -118, 40, -12, -59, -120, -27, 100, -123, -112, 124, -59, 38, 82, -46, -124, -79, 127, 108, 60, 70, 15, 9, 55,
            -111, 8, 91, 119, 92, -90, 87, 67, 86, -72, -65, 112, 47, 68, -57, 15, -113, -53, -6, 61, -82, 26, 113, 78, -2, -60, 10, -65, -23, 92, -8, 123, 114, 65, 82, 91, -3, 12, 127, -97, 92, -18, 118, -28, -80, 98, -122, 96, 11, 61, 102, 53, 23, 13, -104, 89, -56, -67, 106, 49, -36, -38, 48, 45, -102, 27, -110, 85, 124, 26, -46, 11, -35, 83, 98, 17, -40, -57, -27, -11, -50, 88, 52, -14, 33, 127, -44, 97, -3, -51, -72, -97, 77, -109, 80, 125, 79, 107, 6, -62, -15, 93, -58, 100, 16, -94, -23, -17, 71, 36, 35, 99, -93, -27, -99, 86, -107, 12, -97, -53, -54, -7, -119, 126, 69, 75, 6, -108, 66, -67, 95, -125, 42, -11, 117, 117, 126, 72, -18, -37, -61, 65, -64, 43, 30, 59, -16, -42, 6, 99, 93, 13, 37, -17, -96, -50, 17, -90, 9, 85, 25, 86, -4, -17, -32, -85, 118, -101, -4, 100, 55, -128, 24, -65, 63, 118, 10, -41, -52, 38, 117, -107, -42, -74, -100, -29, -48, 64, -97, 126, -116, 56, 70, -116, -25, 97, 6, 105, -22, 4, -72, 41, 101, 117, 28, 88, 121, -125, -67, 22, -32, -92, -26, -35,
            -40, -27, 71, 125, 114, 108, -27, 22, -98, -83, 24, 112, -57, -24, 35, 57, -72, -51, 84, 122, -107, -65, 99, -32, -71, 11, -59, -118, -8, -26, -127, 127, 36, -27, 83, -37, 0, -113, 28, 92, -111, -19, 119, -8, 16, 122, -109, -66, -97, -104, 86, 50, 13, -52, -21, 58, 39, -74, -22, 55, -44, -23, -40, 91, 36, 10, -72, -123, 101, 115, -18, -103, -27, -5, 67, 103, -66, -20, 38, 16, -35, 102, 26, 1, 12, 87, 36, 84, 55, -76, 63, 99, 108, -46, -88, -70, -56, 18, 42, -110, 59, 91, -47, -18, 26, -71, 116, -101, -29, 31, 0, -52, -94, -122, 60, 20, 99, -111, -6, 103, -116, -37, 18, -72, 107, 67, -101, 25, 57, 28, -26, 80, -99, -26, -39, -27, 99, -88, 115, -83, -116, 114, 3, 101, 34, 68, -51, 40, -39, 19, -12, -102, 85, -11, 68, 0, 123, -63, -70, -118, -21, 116, 63, -43, -1, 79, 61, -109, -46, -80, -67, 67, -91, 36, -123, -99, -95, -81, 114, 91, 112, 19, 50, 43, -116, -109, -3, -88, -93, 83, -118, -11, -41, 90, -64, -122, -5, -41, -84, -54, -127, -86, 62, -63, -95, -120, -74, -85, 70,
            56, 45, 71, -124, 48, 74, 67, -28, 58, -111, 56, -27, 51, -71, -108, -104, -24, -116, -107, 17, 57, 9, -102, 95, -41, -95, 109, -42, 65, -109, 18, -126, -65, -65, -2, 76, -24, -39, -124, -31, 121, -95, 40, 90, 60, 124, 71, -78, 69, 51, -126, 26, -106, -20, 109, -83, -9, 114, -53, 17, -31, -26, -116, -37, -59, 16, 123, -110, -25, -98, -101, 1, -75, 54, -73, 25, -86, 18, -11, -44, 58, -121, -110, -2, -25, -73, 4, 39, -91, 87, 116, -112, -80, -19, 89, -62, -38, 116, 9, -82, -112, 67, -113, -23, -104, -87, -46, 64, 63, -97, -2, -91, 30, 15, -28, 19, 71, 92, -48, 94, 16, 74, -103, 3, -19, -2, -8, 28, -104, -62, -117, -45, 88, -92, -11, -38, -75, -61, -13, -72, 113, -128, -28, 116, -120, -36, 34, 124, -76, -41, -92, -61, -128, 116, -123, 29, -102, -128, -67, -59, -104, -20, -26, -16, -59, 26, 51, 63, 49, 64, -99, 12, 48, 35, 118, -19, -85, -52, 13, -32, 102, -26, -30, -107, 5, 68, 124, 39, -28, -117, 78, 53, -111, -31, 35, -89, -71, -39, 59, -81, 9, -53, 13, 68, 103, -116, -79,
            32, -31, 29, -117, -56, -58, -97, -34, 96, -87, 120, -13, 125, -112, 97, -15, -47, 98, 121, -23, 54, -124, 116, 56, 91, 109, 78, 62, -103, -11, -22, -26, 95, 28, 49, -107, 24, 50, 54, 7, 9, -111, 93, -42, -114, -73, 66, 92, 110, 114, 13, -9, -101, -101, 53, 127, -73, -31, -90, 75, 88, -42, 115, 46, 38, -35, -40, 39, 78, -121, 109, -78, 33, 114, 77, -18, 88, -124, 73, 90, 1, -93, -20, -34, -5, -104, -13, -108, -124, -97, 115, -107, -115, -83, -16, -67, 102, 90, 66, 31, 0, 8, 13, 94, 71, 96, -117, 69, 26, -77, -104, -26, -102, 116, -110, -40, -125, -46, 86, -10, -114, 91, -38, 31, 65, 60, 79, -33, 6, -121, -69, -78, -56, 71, 30, -89, -40, -125, -32, -29, -103, -69, -106, 108, -112, 53, -118, -78, 4, 43, -88, 100, 112, -61, -128, 63, -7, 64, -31, 104, -96, -16, 115, 1, 42, -67, 23, 65, 35, -36, 68, -113, -30, 94, -118, 115, -113, -46, -53, -46, 11, 0, 112, -20, -116, 16, 54, -99 };

    private static final String encoded_sha256_key = "SHA256:ZymLIOaVnM+XnyAX1uK4KylBqyH1n6VCGyLauA1ULWv5+0WjfOyf1R9mAruZRv77iQkj1GO8glfIripZ2kVvLY3zfRw3oS1IZcj1yJMqDICGws1FxGyLqUQRw7ZEC+2CYX11l45YpiS6I+kRWdXcm3Si30SfagjbSWikDRVBS78eSO2pN/lBxLb2djisbjToyipf4ym8mp2ewwt4wgtWzC/l9TlbSz1F1onAC2DNjyZQp6+dt+5xg+oBPpWst+DV4MAmwdrqaqMznblcDygBAChpVLAV3O97Ozyvfh8MEMEVBV86c1kgZ+cqhw8oTqKWx/uhehCCLG1olucpxoJYSCAWrX6TL4MjG4prMz1gS8VvwiYuJUP60IC+fceTIDHJNehYjM+GWw7ixvvH2W3vc8Wk8xfwC70jYpZBaXtLFcj9wUWxVDmzT6V8OF+AC5T29eEarUQllLgfxu0ROspIcJDquFinRtnIFem3cW6TuZGSLGj5Naa476Ck0fiezh+Z1sPorVWBfxpnoXtr+U1Kl1UxBD0sfzYUZPCBIbD3iRfkkW5jZgQXoqk0dciBYHdA/eVJuQE14cIWg27dcBWQSF/VUweC8pSDCeJA2vM52miyKN/D7/sVrI1ixD/CmDFiGkYHvV1EewdS8azU1Tc+rvCT9ZAnngXoJjAYfSrwYaR6p8dg3T6uQSflxdeiK6AeR9Lh8Wtp5rGkMYEcgQQbUu49LkEqWIGy5if2vEqfXwgghs31IHFKvxtO6q6UcT++PPJ54DvTP/ecHtY26pIR/Z1vziabXkLkZl5y2UkfA0b7OgOQPcBrU8zyPQM8SROTvNvNIlOA3LUqUCvp9HP7zNG874o/Dy+IoR438XUPk7mx35wwO0lSna1sYgCU5AI/b1Svs7B1SxXfDB9tfm/ho6AMxv2EqDZcQ91gnaodyXpdTqMnJWqnJjzqsi5Y2vwiwfHFSynuKVNg8BlZu3ZvbT+mwKcaUvNY2gem0pv0LfQSQBjrJWHNDOCl7+P0gXnzBHFkEQ3/Hh48rV1zO2s56ck9k69RvLE6Jxj8RjQA9wkZd0nNU/qjdpO6b8Qb55AlWrNrk34oJbY109hHD9NWOlPogEVhF5UxIc1jo7NaKemNF7mO+KbYuDE11YmT5CFxl0wSBzKIcCmOd99YHt21MZNLpaqN8u3GihKU+HrFb3qQN6Y1KAWsPdJ9E+Nf5NcCqFVCtM5H1Wrgm1NoGc4USxZ99D0PJTEod/ZCkGmZoYoCkV4d+Is8f++2SyHYFDvoK8VBPCpMgAoKOmhxOhiD2LzS7nDS+ij60q0nF9yWytf9Ht7CRyJW5X6bXJCq9cidyD503Jc/XdPs2CYPEU+YEg==";

    private static final byte[] sha256_key = { 103, 41, -117, 32, -26, -107, -100, -49, -105, -97, 32, 23, -42, -30, -72, 43, 41, 65, -85, 33, -11, -97, -91, 66, 27, 34, -38, -72, 13, 84, 45, 107, -7, -5, 69, -93, 124, -20, -97, -43, 31, 102, 2, -69, -103, 70, -2, -5, -119, 9, 35, -44, 99, -68, -126, 87, -56, -82, 42, 89, -38, 69, 111, 45, -115, -13, 125, 28, 55, -95, 45, 72, 101, -56, -11, -56, -109, 42, 12, -128, -122, -62, -51, 69, -60, 108, -117, -87, 68, 17, -61, -74, 68, 11, -19, -126, 97, 125, 117, -105, -114, 88, -90, 36, -70, 35, -23, 17, 89, -43, -36, -101, 116, -94, -33, 68, -97, 106, 8, -37, 73, 104, -92, 13, 21, 65, 75, -65, 30, 72, -19, -87, 55, -7, 65, -60, -74, -10, 118, 56, -84, 110, 52, -24, -54, 42, 95, -29, 41, -68, -102, -99, -98, -61, 11, 120, -62, 11, 86, -52, 47, -27, -11, 57, 91, 75, 61, 69, -42, -119, -64, 11, 96, -51, -113, 38, 80, -89, -81, -99, -73, -18, 113, -125, -22, 1, 62, -107, -84, -73, -32, -43, -32, -64, 38, -63, -38, -22, 106, -93, 51, -99, -71, 92, 15,
            40, 1, 0, 40, 105, 84, -80, 21, -36, -17, 123, 59, 60, -81, 126, 31, 12, 16, -63, 21, 5, 95, 58, 115, 89, 32, 103, -25, 42, -121, 15, 40, 78, -94, -106, -57, -5, -95, 122, 16, -126, 44, 109, 104, -106, -25, 41, -58, -126, 88, 72, 32, 22, -83, 126, -109, 47, -125, 35, 27, -118, 107, 51, 61, 96, 75, -59, 111, -62, 38, 46, 37, 67, -6, -48, -128, -66, 125, -57, -109, 32, 49, -55, 53, -24, 88, -116, -49, -122, 91, 14, -30, -58, -5, -57, -39, 109, -17, 115, -59, -92, -13, 23, -16, 11, -67, 35, 98, -106, 65, 105, 123, 75, 21, -56, -3, -63, 69, -79, 84, 57, -77, 79, -91, 124, 56, 95, -128, 11, -108, -10, -11, -31, 26, -83, 68, 37, -108, -72, 31, -58, -19, 17, 58, -54, 72, 112, -112, -22, -72, 88, -89, 70, -39, -56, 21, -23, -73, 113, 110, -109, -71, -111, -110, 44, 104, -7, 53, -90, -72, -17, -96, -92, -47, -8, -98, -50, 31, -103, -42, -61, -24, -83, 85, -127, 127, 26, 103, -95, 123, 107, -7, 77, 74, -105, 85, 49, 4, 61, 44, 127, 54, 20, 100, -16, -127, 33, -80, -9, -119, 23, -28,
            -111, 110, 99, 102, 4, 23, -94, -87, 52, 117, -56, -127, 96, 119, 64, -3, -27, 73, -71, 1, 53, -31, -62, 22, -125, 110, -35, 112, 21, -112, 72, 95, -43, 83, 7, -126, -14, -108, -125, 9, -30, 64, -38, -13, 57, -38, 104, -78, 40, -33, -61, -17, -5, 21, -84, -115, 98, -60, 63, -62, -104, 49, 98, 26, 70, 7, -67, 93, 68, 123, 7, 82, -15, -84, -44, -43, 55, 62, -82, -16, -109, -11, -112, 39, -98, 5, -24, 38, 48, 24, 125, 42, -16, 97, -92, 122, -89, -57, 96, -35, 62, -82, 65, 39, -27, -59, -41, -94, 43, -96, 30, 71, -46, -31, -15, 107, 105, -26, -79, -92, 49, -127, 28, -127, 4, 27, 82, -18, 61, 46, 65, 42, 88, -127, -78, -26, 39, -10, -68, 74, -97, 95, 8, 32, -122, -51, -11, 32, 113, 74, -65, 27, 78, -22, -82, -108, 113, 63, -66, 60, -14, 121, -32, 59, -45, 63, -9, -100, 30, -42, 54, -22, -110, 17, -3, -99, 111, -50, 38, -101, 94, 66, -28, 102, 94, 114, -39, 73, 31, 3, 70, -5, 58, 3, -112, 61, -64, 107, 83, -52, -14, 61, 3, 60, 73, 19, -109, -68, -37, -51, 34, 83, -128, -36, -75,
            42, 80, 43, -23, -12, 115, -5, -52, -47, -68, -17, -118, 63, 15, 47, -120, -95, 30, 55, -15, 117, 15, -109, -71, -79, -33, -100, 48, 59, 73, 82, -99, -83, 108, 98, 0, -108, -28, 2, 63, 111, 84, -81, -77, -80, 117, 75, 21, -33, 12, 31, 109, 126, 111, -31, -93, -96, 12, -58, -3, -124, -88, 54, 92, 67, -35, 96, -99, -86, 29, -55, 122, 93, 78, -93, 39, 37, 106, -89, 38, 60, -22, -78, 46, 88, -38, -4, 34, -63, -15, -59, 75, 41, -18, 41, 83, 96, -16, 25, 89, -69, 118, 111, 109, 63, -90, -64, -89, 26, 82, -13, 88, -38, 7, -90, -46, -101, -12, 45, -12, 18, 64, 24, -21, 37, 97, -51, 12, -32, -91, -17, -29, -12, -127, 121, -13, 4, 113, 100, 17, 13, -1, 30, 30, 60, -83, 93, 115, 59, 107, 57, -23, -55, 61, -109, -81, 81, -68, -79, 58, 39, 24, -4, 70, 52, 0, -9, 9, 25, 119, 73, -51, 83, -6, -93, 118, -109, -70, 111, -60, 27, -25, -112, 37, 90, -77, 107, -109, 126, 40, 37, -74, 53, -45, -40, 71, 15, -45, 86, 58, 83, -24, -128, 69, 97, 23, -107, 49, 33, -51, 99, -93, -77, 90, 41, -23,
            -115, 23, -71, -114, -8, -90, -40, -72, 49, 53, -43, -119, -109, -28, 33, 113, -105, 76, 18, 7, 50, -120, 112, 41, -114, 119, -33, 88, 30, -35, -75, 49, -109, 75, -91, -86, -115, -14, -19, -58, -118, 18, -108, -8, 122, -59, 111, 122, -112, 55, -90, 53, 40, 5, -84, 61, -46, 125, 19, -29, 95, -28, -41, 2, -88, 85, 66, -76, -50, 71, -43, 106, -32, -101, 83, 104, 25, -50, 20, 75, 22, 125, -12, 61, 15, 37, 49, 40, 119, -10, 66, -112, 105, -103, -95, -118, 2, -111, 94, 29, -8, -117, 60, 127, -17, -74, 75, 33, -40, 20, 59, -24, 43, -59, 65, 60, 42, 76, -128, 10, 10, 58, 104, 113, 58, 24, -125, -40, -68, -46, -18, 112, -46, -6, 40, -6, -46, -83, 39, 23, -36, -106, -54, -41, -3, 30, -34, -62, 71, 34, 86, -27, 126, -101, 92, -112, -86, -11, -56, -99, -56, 62, 116, -36, -105, 63, 93, -45, -20, -40, 38, 15, 17, 79, -104, 18, };

    private static final String encoded_sha512_key = "SHA512:XosfmtImdhE6YxgwPntlYxVUomK4xK5rOBO4tq43n70c3HqGJLGkEUmCXrC8VuSZTFtakFwMNq1Q4hMu9tWUuaYT9urv2CPzxaOYDqQ5n1T5hzHBM7wmXz7YtyOoxo7poXMDztfXba2NcdkjYZICgVnafwZSTVQpuh0dUCafSTtNOKC1857MGTuyAE66zQlPFLpkoxP0VJ6ZWA77q7cxB7FAcvztykGiAlemGswokuP6hIokTVCpMzryqKjvHDCuqAih9Yit++nUKbpVAGbqiGCdZ1daTfWHo1mGIf5sB/squWvOf9/tO5ROOUua8o2hKapnL7UdW6iWLl08J8OEGe/M19VdBQkRcxSWfLBZfWiSdJvGT6B4cbdiCn3qGTA/WCImSKpYiDyPAx90pWbFsVMHjgIwLPPgoFRNVzPkEZYDlk45xcHwMQ7n1E6BnVYkMPUOXaaebZCGg9jrjLqfIjAgVbIbPDkM5xrls7TMju/GVh1V2HsYShf/rLf0Y2uPohOPG9ZQ8R4jyKff8RpolvLagyJeXTBZVbrsyja8t7/fbS1t0XTtcfmuLw1qtacfm5uDDeKeJKuPPr7muCzjFL+Xdla4zIgzFaORqFUJPOmdo6+2nqB/MbOupe8BwPBEMhMQK0cpK6DA71XMkj1gXoU7eTkLrFJHfmNybRYZnSATK3EBvwXT7BJJ1slD2CS1UAb8wFPe7yizzSCnBGrNp9D98/r1dy0nll0sZpsuIPPMzR+HNtV0UQcz1Pi497J4qDGSody6lGNugi2U/57JhrWTGR4rmRkMkaCgzA6m0R2nhYWlqGk74Cl8JQ9HBHdFz3PNtkSdfV5sT7FvT+4rMxrj2FEMyRyCXSU7ZcLh8UlT0CS9nnf+BaTsBdASL2z9oVNF5tO2E4PP3zT/nDB8QmZLNwvXCGVtUc01ZZYab4AGqG44e5w33yfc4jg7qvTraUkgNaZHaNm/+uKlrAwC0VFNop4fUIEiYxNLKyGlvveWyTLaC4FVCJ8RPTEPNtUl7kE6hb2t5qdoyOmlChtYBwcwN6/D8R4K2AHYZT5jn+DfX3ZQU7J2gQYmX6n+3XV4MNrfgHc7BqDmGw7/ocROY9eypnjUDNIUpRRTaWRJL2kqn8C67WM+xbMFJ0OYAMi9px9zrxWx8zaGmQItCFDvnGwMY8lcFM8h7MQyp7Fb/PwnkhiPpPAAATcXHbD27lDbFnxPUPAu5vheN3w1LMmPoP2CivgKzRCeAQUq5x+pjqzJlU+DiuPEhNPKurkHd1ccUCpFVcx6owDEmaUEcSfk4uUkJxMAx1tgGjuTlaX5EMUnRzr3rBwj3iBwZdaxX2fdRNoQnPAqnOeazY/P1ZRNfQ==";

    private static final byte[] sha512_key = { 94, -117, 31, -102, -46, 38, 118, 17, 58, 99, 24, 48, 62, 123, 101, 99, 21, 84, -94, 98, -72, -60, -82, 107, 56, 19, -72, -74, -82, 55, -97, -67, 28, -36, 122, -122, 36, -79, -92, 17, 73, -126, 94, -80, -68, 86, -28, -103, 76, 91, 90, -112, 92, 12, 54, -83, 80, -30, 19, 46, -10, -43, -108, -71, -90, 19, -10, -22, -17, -40, 35, -13, -59, -93, -104, 14, -92, 57, -97, 84, -7, -121, 49, -63, 51, -68, 38, 95, 62, -40, -73, 35, -88, -58, -114, -23, -95, 115, 3, -50, -41, -41, 109, -83, -115, 113, -39, 35, 97, -110, 2, -127, 89, -38, 127, 6, 82, 77, 84, 41, -70, 29, 29, 80, 38, -97, 73, 59, 77, 56, -96, -75, -13, -98, -52, 25, 59, -78, 0, 78, -70, -51, 9, 79, 20, -70, 100, -93, 19, -12, 84, -98, -103, 88, 14, -5, -85, -73, 49, 7, -79, 64, 114, -4, -19, -54, 65, -94, 2, 87, -90, 26, -52, 40, -110, -29, -6, -124, -118, 36, 77, 80, -87, 51, 58, -14, -88, -88, -17, 28, 48, -82, -88, 8, -95, -11, -120, -83, -5, -23, -44, 41, -70, 85, 0, 102, -22, -120, 96,
            -99, 103, 87, 90, 77, -11, -121, -93, 89, -122, 33, -2, 108, 7, -5, 42, -71, 107, -50, 127, -33, -19, 59, -108, 78, 57, 75, -102, -14, -115, -95, 41, -86, 103, 47, -75, 29, 91, -88, -106, 46, 93, 60, 39, -61, -124, 25, -17, -52, -41, -43, 93, 5, 9, 17, 115, 20, -106, 124, -80, 89, 125, 104, -110, 116, -101, -58, 79, -96, 120, 113, -73, 98, 10, 125, -22, 25, 48, 63, 88, 34, 38, 72, -86, 88, -120, 60, -113, 3, 31, 116, -91, 102, -59, -79, 83, 7, -114, 2, 48, 44, -13, -32, -96, 84, 77, 87, 51, -28, 17, -106, 3, -106, 78, 57, -59, -63, -16, 49, 14, -25, -44, 78, -127, -99, 86, 36, 48, -11, 14, 93, -90, -98, 109, -112, -122, -125, -40, -21, -116, -70, -97, 34, 48, 32, 85, -78, 27, 60, 57, 12, -25, 26, -27, -77, -76, -52, -114, -17, -58, 86, 29, 85, -40, 123, 24, 74, 23, -1, -84, -73, -12, 99, 107, -113, -94, 19, -113, 27, -42, 80, -15, 30, 35, -56, -89, -33, -15, 26, 104, -106, -14, -38, -125, 34, 94, 93, 48, 89, 85, -70, -20, -54, 54, -68, -73, -65, -33, 109, 45, 109, -47,
            116, -19, 113, -7, -82, 47, 13, 106, -75, -89, 31, -101, -101, -125, 13, -30, -98, 36, -85, -113, 62, -66, -26, -72, 44, -29, 20, -65, -105, 118, 86, -72, -52, -120, 51, 21, -93, -111, -88, 85, 9, 60, -23, -99, -93, -81, -74, -98, -96, 127, 49, -77, -82, -91, -17, 1, -64, -16, 68, 50, 19, 16, 43, 71, 41, 43, -96, -64, -17, 85, -52, -110, 61, 96, 94, -123, 59, 121, 57, 11, -84, 82, 71, 126, 99, 114, 109, 22, 25, -99, 32, 19, 43, 113, 1, -65, 5, -45, -20, 18, 73, -42, -55, 67, -40, 36, -75, 80, 6, -4, -64, 83, -34, -17, 40, -77, -51, 32, -89, 4, 106, -51, -89, -48, -3, -13, -6, -11, 119, 45, 39, -106, 93, 44, 102, -101, 46, 32, -13, -52, -51, 31, -121, 54, -43, 116, 81, 7, 51, -44, -8, -72, -9, -78, 120, -88, 49, -110, -95, -36, -70, -108, 99, 110, -126, 45, -108, -1, -98, -55, -122, -75, -109, 25, 30, 43, -103, 25, 12, -111, -96, -96, -52, 14, -90, -47, 29, -89, -123, -123, -91, -88, 105, 59, -32, 41, 124, 37, 15, 71, 4, 119, 69, -49, 115, -51, -74, 68, -99, 125, 94, 108,
            79, -79, 111, 79, -18, 43, 51, 26, -29, -40, 81, 12, -55, 28, -126, 93, 37, 59, 101, -62, -31, -15, 73, 83, -48, 36, -67, -98, 119, -2, 5, -92, -20, 5, -48, 18, 47, 108, -3, -95, 83, 69, -26, -45, -74, 19, -125, -49, -33, 52, -1, -100, 48, 124, 66, 102, 75, 55, 11, -41, 8, 101, 109, 81, -51, 53, 101, -106, 26, 111, -128, 6, -88, 110, 56, 123, -100, 55, -33, 39, -36, -30, 56, 59, -86, -12, -21, 105, 73, 32, 53, -90, 71, 104, -39, -65, -6, -30, -91, -84, 12, 2, -47, 81, 77, -94, -98, 31, 80, -127, 34, 99, 19, 75, 43, 33, -91, -66, -9, -106, -55, 50, -38, 11, -127, 85, 8, -97, 17, 61, 49, 15, 54, -43, 37, -18, 65, 58, -123, -67, -83, -26, -89, 104, -56, -23, -91, 10, 27, 88, 7, 7, 48, 55, -81, -61, -15, 30, 10, -40, 1, -40, 101, 62, 99, -97, -32, -33, 95, 118, 80, 83, -78, 118, -127, 6, 38, 95, -87, -2, -35, 117, 120, 48, -38, -33, -128, 119, 59, 6, -96, -26, 27, 14, -1, -95, -60, 78, 99, -41, -78, -90, 120, -44, 12, -46, 20, -91, 20, 83, 105, 100, 73, 47, 105, 42, -97, -64,
            -70, -19, 99, 62, -59, -77, 5, 39, 67, -104, 0, -56, -67, -89, 31, 115, -81, 21, -79, -13, 54, -122, -103, 2, 45, 8, 80, -17, -100, 108, 12, 99, -55, 92, 20, -49, 33, -20, -60, 50, -89, -79, 91, -4, -4, 39, -110, 24, -113, -92, -16, 0, 1, 55, 23, 29, -80, -10, -18, 80, -37, 22, 124, 79, 80, -16, 46, -26, -8, 94, 55, 124, 53, 44, -55, -113, -96, -3, -126, -118, -8, 10, -51, 16, -98, 1, 5, 42, -25, 31, -87, -114, -84, -55, -107, 79, -125, -118, -29, -60, -124, -45, -54, -70, -71, 7, 119, 87, 28, 80, 42, 69, 85, -52, 122, -93, 0, -60, -103, -91, 4, 113, 39, -28, -30, -27, 36, 39, 19, 0, -57, 91, 96, 26, 59, -109, -107, -91, -7, 16, -59, 39, 71, 58, -9, -84, 28, 35, -34, 32, 112, 101, -42, -79, 95, 103, -35, 68, -38, 16, -100, -16, 42, -100, -25, -102, -51, -113, -49, -43, -108, 77, 125, };
    
    private static final byte[] data = { 94, -117, 31, -102, -46, 38, 118, 17, 58, 99, 24, 48, 62, 123, 101, 99, 21, 84, -94, 98, -72, -60, -82, 107, 56, 19, -72, -74, -82, 55, -97, -67, 28, -36, 122, -122, 36, -79, -92, 17, 73, -126, 94, -80, -68, 86, -28, -103, 76, 91, 90, -112, 92, 12, 54, -83, 80, -30, 19, 46, -10, -43, -108, -71, -90, 19, -10, -22, -17, -40, 35, -13, -59, -93, -104, 14, -92, 57, -97, 84, -7, -121, 49, -63, 51, -68, 38, 95, 62, -40, -73, 35, -88, -58, -114, -23, -95, 115, 3, -50, -41, -41, 109, -83, -115, 113, -39, 35, 97, -110, 2, -127, 89, -38, 127, 6, 82, 77, 84, 41, -70, 29, 29, 80, 38, -97, 73, 59, 77, 56, -96, -75, -13, -98, -52, 25, 59, -78, 0, 78, -70, -51, 9, 79, 20, -70, 100, -93, 19, -12, 84, -98, -103, 88, 14, -5, -85, -73, 49, 7, -79, 64, 114, -4, -19, -54, 65, -94, 2, 87, -90, 26, -52, 40, -110, -29, -6, -124, -118, 36, 77, 80, -87, 51, 58, -14, -88, -88, -17, 28, 48, -82, -88, 8, -95, -11, -120, -83, -5, -23, -44, 41, -70, 85, 0, 102, -22, -120, 96,
            -99, 103, 87, 90, 77, -11, -121, -93, 89, -122, 33, -2, 108, 7, -5, 42, -71, 107, -50, 127, -33, -19, 59, -108, 78, 57, 75, -102, -14, -115, -95, 41, -86, 103, 47, -75, 29, 91, -88, -106, 46, 93, 60, 39, -61, -124, 25, -17, -52, -41, -43, 93, 5, 9, 17, 115, 20, -106, 124, -80, 89, 125, 104, -110, 116, -101, -58, 79, -96, 120, 113, -73, 98, 10, 125, -22, 25, 48, 63, 88, 34, 38, 72, -86, 88, -120, 60, -113, 3, 31, 116, -91, 102, -59, -79, 83, 7, -114, 2, 48, 44, -13, -32, -96, 84, 77, 87, 51, -28, 17, -106, 3, -106, 78, 57, -59, -63, -16, 49, 14, -25, -44, 78, -127, -99, 86, 36, 48, -11, 14, 93, -90, -98, 109, -112, -122, -125, -40, -21, -116, -70, -97, 34, 48, 32, 85, -78, 27, 60, 57, 12, -25, 26, -27, -77, -76, -52, -114, -17, -58, 86, 29, 85, -40, 123, 24, 74, 23, -1, -84, -73, -12, 99, 107, -113, -94, 19, -113, 27, -42, 80, -15, 30, 35, -56, -89, -33, -15, 26, 104, -106, -14, -38, -125, 34, 94, 93, 48, 89, 85, -70, -20, -54, 54, -68, -73, -65, -33, 109, 45, 109, -47,
            116, -19, 113, -7, -82, 47, 13, 106, -75, -89, 31, -101, -101, -125, 13, -30, -98, 36, -85, -113, 62, -66, -26, -72, 44, -29, 20, -65, -105, 118, 86, -72, -52, -120, 51, 21, -93, -111, -88, 85, 9, 60, -23, -99, -93, -81, -74, -98, -96, 127, 49, -77, -82, -91, -17, 1, -64, -16, 68, 50, 19, 16, 43, 71, 41, 43, -96, -64, -17, 85, -52, -110, 61, 96, 94, -123, 59, 121, 57, 11, -84, 82, 71, 126, 99, 114, 109, 22, 25, -99, 32, 19, 43, 113, 1, -65, 5, -45, -20, 18, 73, -42, -55, 67, -40, 36, -75, 80, 6, -4, -64, 83, -34, -17, 40, -77, -51, 32, -89, 4, 106, -51, -89, -48, -3, -13, -6, -11, 119, 45, 39, -106, 93, 44, 102, -101, 46, 32, -13, -52, -51, 31, -121, 54, -43, 116, 81, 7, 51, -44, -8, -72, -9, -78, 120, -88, 49, -110, -95, -36, -70, -108, 99, 110, -126, 45, -108, -1, -98, -55, -122, -75, -109, 25, 30, 43, -103, 25, 12, -111, -96, -96, -52, 14, -90, -47, 29, -89, -123, -123, -91, -88, 105, 59, -32, 41, 124, 37, 15, 71, 4, 119, 69, -49, 115, -51, -74, 68, -99, 125, 94, 108,
            79, -79, 111, 79, -18, 43, 51, 26, -29, -40, 81, 12, -55, 28, -126, 93, 37, 59, 101, -62, -31, -15, 73, 83, -48, 36, -67, -98, 119, -2, 5, -92, -20, 5, -48, 18, 47, 108, -3, -95, 83, 69, -26, -45, -74, 19, -125, -49, -33, 52, -1, -100, 48, 124, 66, 102, 75, 55, 11, -41, 8, 101, 109, 81, -51, 53, 101, -106, 26, 111, -128, 6, -88, 110, 56, 123, -100, 55, -33, 39, -36, -30, 56, 59, -86, -12, -21, 105, 73, 32, 53, -90, 71, 104, -39, -65, -6, -30, -91, -84, 12, 2, -47, 81, 77, -94, -98, 31, 80, -127, 34, 99, 19, 75, 43, 33, -91, -66, -9, -106, -55, 50, -38, 11, -127, 85, 8, -97, 17, 61, 49, 15, 54, -43, 37, -18, 65, 58, -123, -67, -83, -26, -89, 104, -56, -23, -91, 10, 27, 88, 7, 7, 48, 55, -81, -61, -15, 30, 10, -40, 1, -40, 101, 62, 99, -97, -32, -33, 95, 118, 80, 83, -78, 118, -127, 6, 38, 95, -87, -2, -35, 117, 120, 48, -38, -33, -128, 119, 59, 6, -96, -26, 27, 14, -1, -95, -60, 78, 99, -41, -78, -90, 120, -44, 12, -46, 20, -91, 20, 83, 105, 100, 73, 47, 105, 42, -97, -64,
            -70, -19, 99, 62, -59, -77, 5, 39, 67, -104, 0, -56, -67, -89, 31, 115, -81, 21, -79, -13, 54, -122, -103, 2, 45, 8, 80, -17, -100, 108, 12, 99, -55, 92, 20, -49, 33, -20, -60, 50, -89, -79, 91, -4, -4, 39, -110, 24, -113, -92, -16, 0, 1, 55, 23, 29, -80, -10, -18, 80, -37, 22, 124, 79, 80, -16, 46, -26, -8, 94, 55, 124, 53, 44, -55, -113, -96, -3, -126, -118, -8, 10, -51, 16, -98, 1, 5, 42, -25, 31, -87, -114, -84, -55, -107, 79, -125, -118, -29, -60, -124, -45, -54, -70, -71, 7, 119, 87, 28, 80, 42, 69, 85, -52, 122, -93, 0, -60, -103, -91, 4, 113, 39, -28, -30, -27, 36, 39, 19, 0, -57, 91, 96, 26, 59, -109, -107, -91, -7, 16, -59, 39, 71, 58, -9, -84, 28, 35, -34, 32, 112, 101, -42, -79, 95, 103, -35, 68, -38, 16, -100, -16, 42, -100, -25, -102, -51, -113, -49, -43, -108, 77, 125, };

    private static final String sha1_sig = "SHA1:IKRXHNBTt5viwBJZKl5hi236k1g=";
    private static final String sha256_sig = "SHA256:CTyFZ1qcLhojCnu8OpqzEd/alVxIViXx7xJb7Oap610=";
    private static final String sha512_sig = "SHA512:+5LK/6yDzpnQa3CAy8GIlyPuAreUwk1adW0+4sMKmXLI8fv9GKxti8Zq6wzDFNiraQgBIwcNvxJP1IjTwZGMXg==";
    
    private static final Parcel parcel = testParcel(); 
            
    private final static Parcel testParcel()
    {
        Parcel p = new Parcel("","");
        p.buffer(ByteBuffer.wrap(data));
        return p;
    }
    
    
    @Test
    public void generateSHA1Key()
    {
        PolyakovKey key = PolyakovKey.generate(KeyHash.SHA1, 1024);
        //
        assertThat("A PolyakovKey was returned", key, is(not(nullValue())));
        assertThat("Hash is SHA1", key.getHash(), is(equalTo(KeyHash.SHA1)));
        assertThat("Key is not null", key.getKey(), is(not(nullValue())));
        assertThat("Key is 1024 bytes long", key.getKey().length, is(equalTo(1024)));
    }

    @Test
    public void generateSHA256Key()
    {
        PolyakovKey key = PolyakovKey.generate(KeyHash.SHA256, 1024);
        //
        assertThat("A PolyakovKey was returned", key, is(not(nullValue())));
        assertThat("Hash is SHA256", key.getHash(), is(equalTo(KeyHash.SHA256)));
        assertThat("Key is not null", key.getKey(), is(not(nullValue())));
        assertThat("Key is 1024 bytes long", key.getKey().length, is(equalTo(1024)));
    }

    @Test
    public void generateSHA512Key()
    {
        PolyakovKey key = PolyakovKey.generate(KeyHash.SHA512, 1024);
        //
        assertThat("A PolyakovKey was returned", key, is(not(nullValue())));
        assertThat("Hash is SHA512", key.getHash(), is(equalTo(KeyHash.SHA512)));
        assertThat("Key is not null", key.getKey(), is(not(nullValue())));
        assertThat("Key is 1024 bytes long", key.getKey().length, is(equalTo(1024)));
    }
    
    @Test
    public void generateKey()
    {
        PolyakovKey key = PolyakovKey.generate(1024);
        //
        assertThat("A PolyakovKey was returned", key, is(not(nullValue())));
        assertThat("Hash is SHA512", key.getHash(), is(equalTo(KeyHash.SHA512)));
        assertThat("Key is not null", key.getKey(), is(not(nullValue())));
        assertThat("Key is 1024 bytes long", key.getKey().length, is(equalTo(1024)));
    }

    @Test
    public void generateSHA1Key4096()
    {
        PolyakovKey key = PolyakovKey.generate(KeyHash.SHA256, 4096);
        //
        assertThat("A PolyakovKey was returned", key, is(not(nullValue())));
        assertThat("Hash is SHA1", key.getHash(), is(equalTo(KeyHash.SHA256)));
        assertThat("Key is not null", key.getKey(), is(not(nullValue())));
        assertThat("Key is 1024 bytes long", key.getKey().length, is(equalTo(4096)));
    }

    @Test
    public void canEncodeSHA1Key()
    {
        PolyakovKey key = new PolyakovKey(KeyHash.SHA1, sha1_key);
        //
        assertThat("Hash is SHA1", key.getHash(), is(equalTo(KeyHash.SHA1)));
        assertThat("Encoded key returns a String", key.toString(), is(not(nullValue())));
        assertThat("Encoded key start with 'SHA1:'", key.toString(), startsWith("SHA1:"));
        assertThat("Encoded key matches", key.toString(), is(equalTo(encoded_sha1_key)));
    }

    @Test
    public void canEncodeSHA256Key()
    {
        PolyakovKey key = new PolyakovKey(KeyHash.SHA256, sha256_key);
        //
        assertThat("Hash is SHA1", key.getHash(), is(equalTo(KeyHash.SHA256)));
        assertThat("Encoded key returns a String", key.toString(), is(not(nullValue())));
        assertThat("Encoded key start with 'SHA1:'", key.toString(), startsWith("SHA256:"));
        assertThat("Encoded key matches", key.toString(), is(equalTo(encoded_sha256_key)));
    }

    @Test
    public void canEncodeSHA512Key()
    {
        PolyakovKey key = new PolyakovKey(KeyHash.SHA512, sha512_key);
        //
        assertThat("Hash is SHA1", key.getHash(), is(equalTo(KeyHash.SHA512)));
        assertThat("Encoded key returns a String", key.toString(), is(not(nullValue())));
        assertThat("Encoded key start with 'SHA1:'", key.toString(), startsWith("SHA512:"));
        assertThat("Encoded key matches", key.toString(), is(equalTo(encoded_sha512_key)));
    }

    @Test
    public void canDecodeSHA1Key()
    {
        PolyakovKey key = new PolyakovKey(encoded_sha1_key);
        //
        assertThat("Key Hash function is SHA1", key.getHash(), is(equalTo(KeyHash.SHA1)));
        assertThat("Key matches", key.getKey(), is(equalTo(sha1_key)));
    }
    
    @Test
    public void canDecodeSHA256Key()
    {
        PolyakovKey key = new PolyakovKey(encoded_sha256_key);
        //
        assertThat("Key Hash function is SHA256", key.getHash(), is(equalTo(KeyHash.SHA256)));
        assertThat("Key matches", key.getKey(), is(equalTo(sha256_key)));
    }
    
    @Test
    public void canDecodeSHA512Key()
    {
        PolyakovKey key = new PolyakovKey(encoded_sha512_key);
        //
        assertThat("Key Hash function is SHA512", key.getHash(), is(equalTo(KeyHash.SHA512)));
        assertThat("Key matches", key.getKey(), is(equalTo(sha512_key)));
    }
    
    @Test
    public void canSignSHA1Key()
    {
        PolyakovKey key = new PolyakovKey(KeyHash.SHA1, sha1_key);
        //
        String sig = key.sign(parcel);
        //
        assertThat("Signature matches", sig, is(equalTo(sha1_sig)));
    }
    
    @Test
    public void canSignSHA256Key()
    {
        PolyakovKey key = new PolyakovKey(KeyHash.SHA256, sha256_key);
        //
        String sig = key.sign(parcel);
        //
        assertThat("Signature matches", sig, is(equalTo(sha256_sig)));
    }
    
    @Test
    public void canSignSHA512Key()
    {
        PolyakovKey key = new PolyakovKey(KeyHash.SHA512, sha512_key);
        //
        String sig = key.sign(parcel);
        //
        assertThat("Signature matches", sig, is(equalTo(sha512_sig)));
    }
    
    @Test
    public void canCheckSHA1Key()
    {
        PolyakovKey key = new PolyakovKey(KeyHash.SHA1, sha1_key);
        //
        boolean chk = key.check(parcel, sha1_sig);
        //
        assertThat("Signature matches", chk, is(equalTo(true)));
    }
    
    @Test
    public void canCheckSHA256Key()
    {
        PolyakovKey key = new PolyakovKey(KeyHash.SHA256, sha256_key);
        //
        boolean chk = key.check(parcel, sha256_sig);
        //
        assertThat("Signature matches", chk, is(equalTo(true)));
    }
    
    @Test
    public void canCheckSHA512Key()
    {
        PolyakovKey key = new PolyakovKey(KeyHash.SHA512, sha512_key);
        //
        boolean chk = key.check(parcel, sha512_sig);
        //
        assertThat("Signature matches", chk, is(equalTo(true)));
    }
}
