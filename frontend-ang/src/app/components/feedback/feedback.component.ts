import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {WsClient} from '../../service/ws.client';
import {ActionData} from '../../data/ActionData';

@Component({
  selector: 'feedback-component',
  templateUrl: './feedback.component.template.html',
  styleUrls: ['./feedback.component.style.css']
})
export class FeedbackComponent implements OnInit, OnDestroy {
  @Input() wsClient: WsClient;
  @Input() actions: ActionData[];
  @Input() userName: string;

  private subscribed: boolean = false; // TODO: remove this flag, use rxJX for subscription, not a timeout
  feedbackMessages: Array<object> = [];

  ngOnInit(): void {
    console.log('init feedback component');
    if (this.wsClient) {
      this.subscribeToFeedback();

      if (this.subscribed == false) {
        setTimeout(() => this.subscribeToFeedback(), 1000);
      }
    } else {
      console.log('else feedback');
    }
  }

  private subscribeToFeedback(): void {
    if (this.subscribed === false) {
      this.subscribeToPrivateFeedback();
      this.subscribeToActions();
      this.subscribeToPublicFeedback();
      this.subscribed = true;
    }
  }

  private subscribeToPublicFeedback(): void {
    let thisService = this;
    this.wsClient.subscribeToTopic('/feedback', (msg) => {
      thisService.handleMessage(msg);
    });
  }

  private subscribeToActions(): void {
    let thisService = this;
    this.actions.forEach(function(action) {
      thisService.wsClient.subscribeToTopic('/feedback/action/' + action.executorId, (msg) => {
        thisService.handleMessage(msg);
        });
    });
  }

  private subscribeToPrivateFeedback(): void {
    let thisService = this;
    this.wsClient.subscribeToTopic('/feedback/user/' + this.userName, (msg) => {
      thisService.handleMessage(msg);
    });
  }

  private handleMessage(msg): void {
    //  instanceof Array
    let bodyArray = JSON.parse(msg.body);
    let thisFeedbackMessages = this.feedbackMessages;
    if (Array.isArray(bodyArray)) {
      bodyArray.forEach(function(feedbackMessage) {
        // TODO: establish a common communication protocol here
        thisFeedbackMessages.push(feedbackMessage);
      });
    }
    else {
      // TODO: establish a common communication protocol here
      this.feedbackMessages.push(bodyArray);
    }
  }

  userFeedbackStyle(feedbackUserName) {
    let isCurrentUser = feedbackUserName == this.userName;
    return {
      'current-username': isCurrentUser,
      'different-username': !isCurrentUser,
    };
  }

  getFeedbackTypeClass(feedback) {
    return {
      'unsubscribed-board': feedback.type == 'unsubscribed'
    }
  }

  ngOnDestroy(): void {
    // unsubscribe from /topic/feedback
  }

}
