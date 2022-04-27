import { Component, OnInit, Input } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Faq } from '../Faq';

@Component({
    selector: 'app-faq-item',
    templateUrl: './faq-item.component.html',
    styleUrls: ['./faq-item.component.scss']
})
export class FaqItemComponent {
	@Input() sporsmaal: any;
	likes: number;
	teller: number = 0;
	harPressetLikeKnapp: boolean = false;
	harPressetDislikeKnapp: boolean = false;
	isExpanded: boolean = false;

	constructor(private _http: HttpClient) {}

	ngOnInit() {
		this.likes = this.sporsmaal.rating;
	}
		
	toggle(e) {
		e.currentTarget.blur();
		this.isExpanded = !this.isExpanded;
	}
	
	thumbsUp() {
		if (this.harPressetDislikeKnapp) {
			this.thumbsDown();
		} 
		else {
			this.teller = 0;
		}
		if (this.harPressetLikeKnapp === false) {
			this.harPressetLikeKnapp = true;
			this.likes += 1;
			this.teller += 1;
		} else {
			this.harPressetLikeKnapp = false;
			this.likes -= 1;
			this.teller -= 1;
		}
	}

	thumbsDown() {
		if (this.harPressetLikeKnapp) {
			this.thumbsUp();
		} 
		else {
			this.teller = 0;
		}
		if (this.harPressetDislikeKnapp === false) {
			this.harPressetDislikeKnapp = true;
			this.likes -= 1;
			this.teller -= 1;
		} else {
			this.harPressetDislikeKnapp = false;
			this.likes += 1;
			this.teller += 1;
		}
	}

	handleRating(e) {
		if (e.currentTarget.id === "up") {
			this.thumbsUp();
		}
		if (e.currentTarget.id === "down") {
			this.thumbsDown();
		}
		this.oppdaterRating(this.teller);
	}

	oppdaterRating(num) {
		const rating: number = num;
		const nyRating = new Faq;
		nyRating.id = this.sporsmaal.id;
		nyRating.rating = rating;
		this._http.put("api/faq/", nyRating)
        .subscribe(
          retur => {},
          error => console.log(error)
    	);
	}
}