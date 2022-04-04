using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace oblig1.Models
{
    public class Bestilling
    {
        public string Epost { get; set; }
        public string Telefon { get; set; }
        public string Fra { get; set; }
        public string Til { get; set; }
        public DateTime BestillingsDato { get; set; }
        public string Reisende { get; set; }
        public DateTime ReiseDato { get; set; }
        public int Rute { get; set; }
        public int TotalPris { get; set; }

        public int getUkedag()
        {
            int innDag = (int)ReiseDato.DayOfWeek;
            int[] dager = {7, 1, 2, 3, 4, 5, 6};
            int dag = dager[innDag];
            return dag;
        }
    }
}
