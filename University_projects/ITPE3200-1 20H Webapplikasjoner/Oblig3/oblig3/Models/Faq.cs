using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace oblig3.Models
{
    public class Faq
    {
        public int Id { get; set; }
        
        public string Spors { get; set; }
        public string Svar { get; set; }
        public int Rating { get; set; }
        public string Kategori { get; set; }
    }
}
